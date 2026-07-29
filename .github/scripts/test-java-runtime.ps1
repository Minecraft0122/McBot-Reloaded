param(
    [Parameter(Mandatory = $true)]
    [ValidateSet('fabric', 'forge', 'neoforge')]
    [string] $Loader,

    [Parameter(Mandatory = $true)]
    [ValidatePattern('^1\.[0-9]+(?:\.[0-9]+)?$')]
    [string] $MinecraftVersion,

    [Parameter(Mandatory = $true)]
    [ValidateNotNullOrEmpty()]
    [string] $LoaderVersion,

    [Parameter(Mandatory = $true)]
    [ValidateSet(21, 25)]
    [int] $ExpectedJava,

    [ValidateRange(60, 600)]
    [int] $TimeoutSeconds = 300
)

$ErrorActionPreference = 'Stop'
$ProgressPreference = 'SilentlyContinue'

$repositoryRoot = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..\..'))
$buildJavaHome = [IO.Path]::GetFullPath($env:JAVA_HOME)
$runtimeJavaHome = [IO.Path]::GetFullPath($env:MCBOT_RUNTIME_JAVA_HOME)
$isWindowsPlatform = [Environment]::OSVersion.Platform -eq [PlatformID]::Win32NT
$javaName = if ($isWindowsPlatform) { 'java.exe' } else { 'java' }
$buildJava = Join-Path $buildJavaHome "bin\$javaName"
$runtimeJava = Join-Path $runtimeJavaHome "bin\$javaName"

function Invoke-Download {
    param(
        [Parameter(Mandatory = $true)] [string] $Uri,
        [Parameter(Mandatory = $true)] [string] $OutFile,
        [int] $Attempts = 5
    )

    $curl = Get-Command 'curl.exe' -ErrorAction SilentlyContinue
    if ($curl) {
        & $curl.Source --location --fail --silent --show-error --retry $Attempts --retry-delay 2 --retry-all-errors --output $OutFile $Uri
        if ($LASTEXITCODE -eq 0) { return }
        throw "curl failed to download $Uri with exit code $LASTEXITCODE."
    }

    for ($attempt = 1; $attempt -le $Attempts; $attempt++) {
        try {
            Invoke-WebRequest -Uri $Uri -OutFile $OutFile
            return
        } catch {
            if ($attempt -eq $Attempts) { throw }
            Write-Warning "Download attempt $attempt failed for $Uri. Retrying..."
            Start-Sleep -Seconds ([Math]::Min(2 * $attempt, 10))
        }
    }
}

foreach ($requiredFile in @($buildJava, $runtimeJava)) {
    if (-not (Test-Path -LiteralPath $requiredFile -PathType Leaf)) {
        throw "Required Java executable is missing: $requiredFile"
    }
}

$versionInfo = [Diagnostics.ProcessStartInfo]::new()
$versionInfo.FileName = $runtimeJava
$versionInfo.Arguments = '-version'
$versionInfo.UseShellExecute = $false
$versionInfo.RedirectStandardOutput = $true
$versionInfo.RedirectStandardError = $true
$versionInfo.CreateNoWindow = $true
$versionProcess = [Diagnostics.Process]::Start($versionInfo)
$versionStandardOutput = $versionProcess.StandardOutput.ReadToEnd()
$versionStandardError = $versionProcess.StandardError.ReadToEnd()
$versionProcess.WaitForExit()
$runtimeVersionExitCode = $versionProcess.ExitCode
$runtimeVersion = $versionStandardOutput + $versionStandardError
Write-Host "Server runtime JDK:`n$runtimeVersion"
if ($runtimeVersionExitCode -ne 0) {
    throw "The server Java executable exited with code $runtimeVersionExitCode."
}
if ($runtimeVersion -notmatch ('version "' + $ExpectedJava + '[."]')) {
    throw "The server JDK is not Java $ExpectedJava."
}

$versionMatch = Select-String -LiteralPath (Join-Path $repositoryRoot 'gradle.properties') -Pattern '^mod_version=(.+)$'
if (-not $versionMatch) {
    throw 'gradle.properties does not define mod_version.'
}
$modVersion = $versionMatch.Matches[0].Groups[1].Value.Trim()
$modJar = Join-Path $repositoryRoot "$Loader\build\libs\McBot-$MinecraftVersion-$modVersion-$Loader.jar"
if (-not (Test-Path -LiteralPath $modJar -PathType Leaf)) {
    throw "Production mod JAR is missing: $modJar"
}

$serverDirectory = Join-Path $repositoryRoot "build\java-runtime\$Loader-java-$ExpectedJava"
$modsDirectory = Join-Path $serverDirectory 'mods'
New-Item -ItemType Directory -Path $modsDirectory -Force | Out-Null
Get-ChildItem -LiteralPath $modsDirectory -File -Filter 'McBot-*.jar' -ErrorAction SilentlyContinue |
    Remove-Item -Force
Copy-Item -LiteralPath $modJar -Destination $modsDirectory -Force

Set-Content -LiteralPath (Join-Path $serverDirectory 'eula.txt') -Value 'eula=true' -Encoding ascii
@(
    'online-mode=false'
    'server-port=0'
    'motd=McBot Java runtime smoke test'
    'view-distance=2'
    'simulation-distance=2'
) | Set-Content -LiteralPath (Join-Path $serverDirectory 'server.properties') -Encoding ascii

$serverArguments = [Collections.Generic.List[string]]::new()
$serverArguments.Add('-Xms512M')
$serverArguments.Add('-Xmx1G')

if ($Loader -eq 'fabric') {
    $fabricLauncher = Join-Path $serverDirectory 'fabric-server-launch.jar'
    $fabricLauncherUrl = "https://meta.fabricmc.net/v2/versions/loader/$MinecraftVersion/$LoaderVersion/1.0.1/server/jar"
    Invoke-Download -Uri $fabricLauncherUrl -OutFile $fabricLauncher
    $serverArguments.Add('-jar')
    $serverArguments.Add($fabricLauncher)
} else {
    if ($Loader -eq 'forge') {
        $loaderCoordinate = "$MinecraftVersion-$LoaderVersion"
        $installerUrl = "https://maven.minecraftforge.net/net/minecraftforge/forge/$loaderCoordinate/forge-$loaderCoordinate-installer.jar"
        $argumentsFile = Join-Path $serverDirectory "libraries\net\minecraftforge\forge\$loaderCoordinate\win_args.txt"
    } else {
        $loaderCoordinate = $LoaderVersion
        $installerUrl = "https://maven.neoforged.net/releases/net/neoforged/neoforge/$loaderCoordinate/neoforge-$loaderCoordinate-installer.jar"
        $argumentsFile = Join-Path $serverDirectory "libraries\net\neoforged\neoforge\$loaderCoordinate\win_args.txt"
    }

    $installer = Join-Path $serverDirectory "$Loader-installer.jar"
    Invoke-Download -Uri $installerUrl -OutFile $installer
    $savedErrorActionPreference = $ErrorActionPreference
    $ErrorActionPreference = 'Continue'
    & $buildJava -jar $installer --installServer $serverDirectory 2>&1 | ForEach-Object { Write-Host $_ }
    $installerExitCode = $LASTEXITCODE
    $ErrorActionPreference = $savedErrorActionPreference
    if ($installerExitCode -ne 0) {
        throw "$Loader server installer exited with code $installerExitCode."
    }
    if (-not (Test-Path -LiteralPath $argumentsFile -PathType Leaf)) {
        throw "Loader arguments file is missing: $argumentsFile"
    }
    $serverArguments.Add("@$argumentsFile")
}

$serverArguments.Add('nogui')
$startTime = Get-Date
$processInfo = [Diagnostics.ProcessStartInfo]::new()
$processInfo.FileName = $runtimeJava
$processInfo.WorkingDirectory = $serverDirectory
$processInfo.UseShellExecute = $false
$processInfo.RedirectStandardInput = $true
$processInfo.RedirectStandardOutput = $true
$processInfo.RedirectStandardError = $true
$processInfo.CreateNoWindow = $true
if ($processInfo.PSObject.Properties['ArgumentList']) {
    foreach ($argument in $serverArguments) {
        $processInfo.ArgumentList.Add($argument)
    }
} else {
    $processInfo.Arguments = ($serverArguments | ForEach-Object {
        '"' + $_.Replace('"', '\"') + '"'
    }) -join ' '
}

$process = [Diagnostics.Process]::new()
$process.StartInfo = $processInfo
if (-not $process.Start()) {
    throw 'Unable to start the production server process.'
}

$standardOutputTask = $process.StandardOutput.ReadToEndAsync()
$standardErrorTask = $process.StandardError.ReadToEndAsync()
$started = $false
$runtimeConfirmed = $false
$startupTimedOut = $false
$shutdownTimedOut = $false
$deadline = $startTime.AddSeconds($TimeoutSeconds)
$latestLog = Join-Path $serverDirectory 'logs\latest.log'

while ((Get-Date) -lt $deadline) {
    if (Test-Path -LiteralPath $latestLog -PathType Leaf) {
        $logFile = Get-Item -LiteralPath $latestLog
        if ($logFile.LastWriteTime -ge $startTime) {
            $logText = Get-Content -LiteralPath $latestLog -Raw -Encoding utf8
            if ($logText -match 'McBot Java .*?([0-9]+)') {
                $runtimeConfirmed = [int] $Matches[1] -eq $ExpectedJava
            }
            if ($logText -match 'Done \([^)]+\)! For help') {
                $started = $true
            }
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

if ($started -and $runtimeConfirmed -and -not $process.HasExited) {
    $process.StandardInput.WriteLine('stop')
    $process.StandardInput.Flush()
    if (-not $process.WaitForExit(60000)) {
        $shutdownTimedOut = $true
    }
} elseif (-not $process.HasExited) {
    $startupTimedOut = $true
}

if (-not $process.HasExited) {
    try {
        $process.Kill($true)
    } catch [System.Management.Automation.MethodException] {
        $process.Kill()
    }
    $process.WaitForExit()
}

$standardOutput = $standardOutputTask.GetAwaiter().GetResult()
$standardError = $standardErrorTask.GetAwaiter().GetResult()
Write-Host $standardOutput
if ($standardError) {
    Write-Host $standardError
}

$combinedLogs = if (Test-Path -LiteralPath $latestLog -PathType Leaf) {
    Get-Content -LiteralPath $latestLog -Raw -Encoding utf8
} else {
    ''
}
Write-Host $combinedLogs

if ($startupTimedOut) {
    throw "The server did not finish starting within $TimeoutSeconds seconds."
}
if ($shutdownTimedOut) {
    throw 'The server did not exit within 60 seconds after the stop command.'
}
if ($process.ExitCode -ne 0) {
    throw "The server process exited with code $($process.ExitCode)."
}
if (-not $started) {
    throw 'The server never reached the completed startup state.'
}
if (-not $runtimeConfirmed) {
    throw "The server log did not confirm McBot on Java $ExpectedJava."
}

$fatalPatterns = @(
    'UnsupportedClassVersionError'
    'Unsupported class file major version'
    'InaccessibleObjectException'
    'ExceptionInInitializerError'
    'NoSuchMethodError'
    'NoClassDefFoundError'
    'LinkageError'
    'Failed to load config:'
)
foreach ($fatalPattern in $fatalPatterns) {
    if ($combinedLogs -match $fatalPattern) {
        throw "The server log contains a Java compatibility failure: $fatalPattern"
    }
}

Write-Host "Java $ExpectedJava / $Loader passed production server installation, startup, McBot loading, and clean shutdown."
