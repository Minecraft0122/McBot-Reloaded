# McBot Reloaded（Minecraft 1.7.10 Forge）

这是 McBot 的 Minecraft 1.7.10 Forge 维护分支，用于在 Minecraft 服务器与 QQ 群之间通过 OneBot 11 正向 WebSocket 双向转发消息。

当前维护者为 **Minecraft0122**。代码源自 cnlimiter 与 Nova Committee 的 McBot/Bot-Connect，原作者、许可证和 Git 历史依照 GPL-3.0-or-later 保留。

## 兼容性

| 项目 | 支持范围 |
| --- | --- |
| Minecraft | 1.7.10 |
| Forge | 10.13.4.1614 |
| Java | 标准 Forge 环境使用 Java 8；GTNH 的 Java 21 启动方式需要单独回归验证 |
| OneBot | OneBot 11 正向 WebSocket；字符串或消息段数组上报；`ws://`/`wss://` |

3.0.0 修复了 NapCat 数组消息无法反序列化、普通文本含方括号时被吞掉、`addGroup`/`removeGroup` 群号解析空指针，以及未连接时停服清理空指针。

## 安装

1. 从 [GitHub Actions](https://github.com/Minecraft0122/McBot-Reloaded/actions) 下载 1.7.10 构建，或自行构建。
2. 将 `McBot-1.7.10-3.0.0.jar` 放入服务端 `mods` 目录。
3. 启动一次服务器，编辑生成的 `config/botapi/botapi.json`。
4. 在 NapCatQQ、Lagrange.OneBot 等实现中启用 OneBot 11 正向 WebSocket。
5. 设置机器人地址、QQ 号和互通群，或使用 `/mcbot` 命令。

建议保留 NapCat 的数组消息上报；本分支会把文本、图片、`@`、回复等消息段转换为游戏可显示内容。地址可写为 `127.0.0.1:3001`、`ws://127.0.0.1:3001` 或 `wss://bot.example.com/onebot`。

## 常用命令

| 命令 | 说明 |
| --- | --- |
| `/mcbot addGroup <群号>` | 添加互通 QQ 群 |
| `/mcbot removeGroup <群号>` | 删除互通 QQ 群 |
| `/mcbot connect cqhttp [地址]` | 连接普通 OneBot 实现 |
| `/mcbot disconnect` | 断开连接 |
| `/mcbot status` | 查看状态 |
| `/mcbot help` | 查看完整帮助 |

## 构建

该旧版工程使用 Gradle 4.0 与 Java 8：

```bash
./gradlew clean build
```

最终产物位于 `build/libs/`。旧 ForgeGradle 依赖可能因外部仓库变化而失效，GitHub Actions 会持续巡检构建状态。

## 反馈与许可

请在 [Minecraft0122/McBot-Reloaded Issues](https://github.com/Minecraft0122/McBot-Reloaded/issues) 提交问题，并注明 Minecraft 1.7.10、Forge/Java/McBot 版本、完整日志、NapCat 消息格式和脱敏配置。

项目依据 [GNU GPL 3.0 或更高版本](LICENSE) 发布。
