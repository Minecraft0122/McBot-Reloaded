# McBot Reloaded（Fabric 兼容维护）

这是旧版 Fabric 聚合源码，由 **Minecraft0122** 继续维护。代码源自 cnlimiter 与 Nova Committee，原作者、许可证与 Git 历史依照 GPL-3.0-or-later 保留。

## 包含的 Minecraft 版本

1.16.5、1.18.2、1.19.2、1.19.3、1.19.4、1.20.1、1.20.4、1.21.0。正式面向普通用户的版本以仓库 [Release](https://github.com/Minecraft0122/McBot-Reloaded/releases) 和根分支支持表为准；聚合分支用于修复与回归，不保证每个历史组合都可直接安装。

| Minecraft | 运行 Java |
| --- | --- |
| 1.16.5 | 8 或更高 |
| 1.18.2–1.20.4 | 17 或更高 |
| 1.21.0 | 21 或更高 |

3.0.0 已回移以下维护项：

- 当前维护者、仓库与问题地址改为 Minecraft0122；
- OneBot 地址支持 `ws://`、`wss://` 和无协议写法；
- 停服时终止重连并关闭后台线程，避免服务端进程无法退出；
- 使用 OneBot Client 官方发布地址，并将该 Java 8 兼容依赖打入目标 JAR；
- 版本号和 JAR 命名去除旧 `v`、`release`、`multi` 等发布前缀。

## 构建

工程位于 `fabric/`，使用 Java 21 与 Gradle Wrapper。例如构建 1.19.2：

```bash
cd fabric
./gradlew :1.19.2:build --no-daemon --stacktrace
```

若构建某个历史版本失败，请提交完整 Gradle 日志，并注明 Java 与目标 Minecraft 版本。不同 Minecraft 版本的 JAR 不可混用。

问题请提交到 [Minecraft0122/McBot-Reloaded Issues](https://github.com/Minecraft0122/McBot-Reloaded/issues)。
