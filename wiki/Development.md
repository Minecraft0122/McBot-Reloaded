# 开发与构建

## 仓库结构

正式分支采用多加载器结构：

```text
common/       通用逻辑、配置、OneBot、命令和测试
fabric/       Fabric 入口、Mixin 和打包
forge/        1.20.1 Forge 入口和打包
neoforge/     1.21.1 NeoForge 入口和打包
.github/      CI、自动发布和问题模板
wiki/         GitHub Wiki 的版本控制源文件
```

1.20.1 分支包含 Fabric/Forge；1.21.1 分支包含 Fabric/NeoForge。历史版本使用独立维护分支或聚合预处理构建，不应把正式分支的依赖版本直接复制过去。

## 构建要求

| 目标 | Gradle/编译 JDK | 持续验证的服务端运行 Java |
| --- | --- | --- |
| Minecraft 1.20.1 | 17 | 21、25；最低运行版本仍为 17 |
| Minecraft 1.21.1 | 21 | 21、25 |
| Minecraft 1.18.2/1.19.2 | 17 | 按维护分支说明为准 |
| Minecraft 1.7.10/1.12.2/1.16.5 | 通常为 8 | 按维护分支说明为准 |

不要依赖本机全局 Gradle，使用仓库中的 Gradle Wrapper。

Linux/macOS：

```bash
./gradlew clean build
```

Windows：

```powershell
.\gradlew.bat clean build
```

正式 1.20.1 分支产物位于：

```text
fabric/build/libs/
forge/build/libs/
```

1.21.1 分支的 NeoForge 产物位于 `neoforge/build/libs/`。

## 测试

通用测试位于 `common/src/test/`，覆盖 CQ 消息、Minecraft 文本清理、命令参数、配置 URL、事件和 Bot API。提交连接、消息格式或自定义命令修改时，应增加相应回归测试。

推荐至少运行：

```powershell
.\gradlew.bat clean test build --no-daemon --stacktrace
```

正式分支的 GitHub Actions 还会对每个正式加载器分别使用 Java 21、25 启动专用服务器。Gradle 保持运行在 Minecraft 对应的基准 JDK 上并生成正式重映射 JAR，测试脚本随后安装官方 Fabric、Forge 或 NeoForge 服务端，再由待测 JDK 启动生产形态的 JAR；测试要求服务端启动到 `Done`、日志确认 McBot 的实际 Java 主版本，并通过 `stop` 正常退出，同时拒绝常见字节码、反射和链接错误。

## 代码质量约定

- 不在 Minecraft 服务端主线程等待 WebSocket 连接或远程 API。
- 事件注册和初始化必须可重复调用而不产生重复监听。
- 停服时取消连接并关闭线程池，等待必要的数据保存任务。
- QQ 号和群号使用 Long/字符串安全转换，不使用 Integer。
- OneBot `message` 同时考虑 CQ 字符串与消息段数组。
- 发往 QQ 的游戏文本先清理 Minecraft/ANSI 格式码，并保留必要换行。
- 修改配置命令时同步考虑保存、热重载和错误提示。
- 日志不得记录 Token、Cookie、二维码内容或其他凭据。
- 对不同 Minecraft 版本使用真实 API 适配，不通过只改元数据伪造兼容。

## 多版本维护

- 正式支持：`1.20.1`、`1.21.1`。
- 兼容维护：`1.7.10`、`fabric`、`forge` 等分支。
- 上游问题和 PR 的处理记录见主仓库中的 `UPSTREAM_ISSUES.md` 与 `UPSTREAM_PULLS.md`。
- 回移修复时优先处理崩服、无法停服、重复转发、数据损坏和安全问题。

## 贡献流程

1. 从目标版本分支创建功能分支。
2. 保持改动聚焦，避免同时格式化无关文件。
3. 增加测试并完成干净构建。
4. 更新用户可见文档、变更日志和 Wiki 源文件。
5. 提交 Pull Request，说明影响版本、复现方式和验证结果。

项目依据 [GPL-3.0-or-later](https://github.com/Minecraft0122/McBot-Reloaded/blob/1.20.1/LICENSE)发布。修改和再分发必须继续满足许可证要求，并保留原作者和历史贡献者署名。
