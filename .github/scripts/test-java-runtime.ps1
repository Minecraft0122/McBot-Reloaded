param(
    [Parameter(Mandatory = $true)]
    [ValidateSet('fabric', 'forge', 'neoforge')]
    [string] $Loader,

    [Parameter(Mandatory = $true)]
    [ValidateSet(21, 25)]
    [int] $ExpectedJava,

    [ValidateRange(60, 600)]
    [int] $TimeoutSeconds = 300
)

$ErrorActionPreference = 'Stop'
$ProgressPreference = 'SilentlyContinue'

$repositoryRoot = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..\..'))
$gradleJavaHome = [IO.Path]::GetFullPath($env:JAVA_HOME)
$runtimeJavaHome = [IO.Path]::GetFullPath($env:MCBOT_RUNTIME_JAVA_HOME)
$isWindowsPlatform = [Environment]::OSVersion.Platform -eq [PlatformID]::Win32NT
$javaName = if ($isWindowsPlatform) { 'java.exe' } else { 'java' }
$gradleJava = Join-Path $gradleJavaHome "bin\$javaName"
$runtimeJava = Join-Path $runtimeJavaHome "bin\$javaName"
$wrapperJar = Join-Path $repositoryRoot 'gradle\wrapper\gradle-wrapper.jar'

foreach ($requiredFile in @($gradleJava, $runtimeJava, $wrapperJar)) {
    if (-not (Test-Path -LiteralPath $requiredFile -PathType Leaf)) {
        throw "Required Java runtime test file is missing: $requiredFile"
    }
}

$runtimeVersion = (& $runtimeJava -version 2>&1 | Out-String)
Write-Host "Server runtime JDK:`n$runtimeVersion"
if ($runtimeVersion -notmatch ('version "' + $ExpectedJava + '[."]')) {
    throw "The server JDK is not Java $ExpectedJava."
}

$runDirectories = @(
    (Join-Path $repositoryRoot 'run'),
    (Join-Path $repositoryRoot "$Loader\run")
)
foreach ($runDirectory in $runDirectories) {
    New-Item -ItemType Directory -Path $runDirectory -Force | Out-Null
    Set-Content -LiteralPath (Join-Path $runDirectory 'eula.txt') -Value 'eula=true' -Encoding ascii
    @(
        'online-mode=false'
        'server-port=0'
        'motd=McBot Java runtime smoke test'
        'view-distance=2'
        'simulation-distance=2'
    ) | Set-Content -LiteralPath (Join-Path $runDirectory 'server.properties') -Encoding ascii
}

$startTime = Get-Date
$processInfo = [Diagnostics.ProcessStartInfo]::new()
$processInfo.FileName = $gradleJava
$processInfo.WorkingDirectory = $repositoryRoot
$processInfo.UseShellExecute = $false
$processInfo.RedirectStandardInput = $true
$processInfo.RedirectStandardOutput = $true
$processInfo.RedirectStandardError = $true
$processInfo.CreateNoWindow = $true

foreach ($argument in @(
    '-classpath',
    $wrapperJar,
    'org.gradle.wrapper.GradleWrapperMain',
    ":${Loader}:runServer",
    '--no-daemon',
    '--console=plain',
    '--stacktrace'
)) {
    $processInfo.ArgumentList.Add($argument)
}

$process = [Diagnostics.Process]::new()
$process.StartInfo = $processInfo
if (-not $process.Start()) {
    throw 'Unable to start the Gradle server test process.'
}

$standardOutputTask = $process.StandardOutput.ReadToEndAsync()
$standardErrorTask = $process.StandardError.ReadToEndAsync()
$started = $false
$runtimeConfirmed = $false
$deadline = $startTime.AddSeconds($TimeoutSeconds)

try {
    while ((Get-Date) -lt $deadline) {
        $latestLogs = @(
            foreach ($runDirectory in $runDirectories) {
                $latestLog = Join-Path $runDirectory 'logs\latest.log'
                if (Test-Path -LiteralPath $latestLog -PathType Leaf) {
                    Get-Item -LiteralPath $latestLog
                }
            }
        ) | Where-Object LastWriteTime -ge $startTime

        foreach ($latestLog in $latestLogs) {
            $logText = Get-Content -LiteralPath $latestLog.FullName -Raw -Encoding utf8
            if ($logText -match 'McBot Java .*?([0-9]+)') {
                $runtimeConfirmed = [int] $Matches[1] -eq $ExpectedJava
            }
            if ($logText -match 'Done \([^)]+\)! For help') {
                $started = $true
            }
        }

        if ($started -and $runtimeConfirmed) {
            break
        }
        if ($process.HasExited) {
            break
        }
        Start-Sleep -Seconds 2
    }

    if ($started -and -not $process.HasExited) {
        $process.StandardInput.WriteLine('stop')
        $process.StandardInput.Flush()
    }

    if (-not $process.WaitForExit(60000)) {
        throw 'The server did not exit within 60 seconds after the stop command.'
    }
} finally {
    if (-not $process.HasExited) {
        $process.Kill($true)
        $process.WaitForExit()
    }
}

$standardOutput = $standardOutputTask.GetAwaiter().GetResult()
$standardError = $standardErrorTask.GetAwaiter().GetResult()
Write-Host $standardOutput
if ($standardError) {
    Write-Host $standardError
}

$combinedLogs = @(
    foreach ($runDirectory in $runDirectories) {
        $latestLog = Join-Path $runDirectory 'logs\latest.log'
        if (Test-Path -LiteralPath $latestLog -PathType Leaf) {
            Get-Content -LiteralPath $latestLog -Raw -Encoding utf8
        }
    }
) -join "`n"

if ($process.ExitCode -ne 0) {
    throw "The server test process exited with code $($process.ExitCode)."
}
if (-not $started) {
    throw "The server did not finish starting within $TimeoutSeconds seconds."
}
if (-not $runtimeConfirmed) {
    throw "The server log did not confirm McBot on Java $ExpectedJava."
}

$fatalPatterns = @(
    'UnsupportedClassVersionError',
    'InaccessibleObjectException',
    'ExceptionInInitializerError',
    'NoSuchMethodError',
    'NoClassDefFoundError: cn/evole/mods/mcbot',
    'LinkageError'
)
foreach ($fatalPattern in $fatalPatterns) {
    if ($combinedLogs -match $fatalPattern) {
        throw "The server log contains a Java compatibility failure: $fatalPattern"
    }
}

Write-Host "Java $ExpectedJava / $Loader passed startup, McBot loading, and clean shutdown."
