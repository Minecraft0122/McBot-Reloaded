# McBot Reloaded（Minecraft 1.21.1）

McBot Reloaded 是一个基于 [OneBot 11](https://github.com/botuniverse/onebot-11) 的 Minecraft 服务端模组，用于在 Minecraft 服务器与 QQ 群之间双向转发消息，并允许群成员查询服务器状态或执行获准的命令。

本分支由 **Minecraft0122** 维护，面向 Minecraft 1.21.1。代码源自 [Nova-Committee/McBot](https://github.com/Nova-Committee/McBot)，原作者与历史贡献者署名依照 GPL-3.0-or-later 保留。

## 下载与兼容性

请从 [GitHub Releases](https://github.com/Minecraft0122/McBot-Reloaded/releases) 下载，不要下载 GitHub 自动生成的 Source code 压缩包。

| 项目 | 支持范围 |
| --- | --- |
| Minecraft | **仅 1.21.1** |
| Java | 21 或更高版本；构建目标为 Java 21 |
| Fabric | Loader 0.16.10+、Fabric API 0.115.0+1.21.1 或更高的 1.21.1 版本 |
| NeoForge | 21.1.84 或兼容的 21.1.x 版本 |
| OneBot | OneBot 11 正向 WebSocket，支持 `ws://` 与 `wss://` |

- Fabric 使用文件名以 `-fabric.jar` 结尾的产物，并安装 Fabric API。
- NeoForge 使用文件名以 `-neoforge.jar` 结尾的产物。
- 不要同时安装两个加载器的产物，也不要在其他 Minecraft 版本中使用这些 JAR。
- Jupiter 配置库和 OneBot Client 已内置；Mod Menu 11.0.3+ 是 Fabric 的可选依赖。

## 安装与连接

1. 将对应加载器的 McBot JAR 放入服务器 `mods` 目录；Fabric 还需安装 Fabric API。
2. 启动一次服务器，生成 `mcbot/config.json` 和 `mcbot/cmds/`。
3. 在 NapCatQQ、Lagrange.OneBot 等实现中启用 OneBot 11 正向 WebSocket。
4. 在配置文件或游戏内命令中设置地址、机器人 QQ、令牌和互通群号。
5. 执行 `/mcbot connect`，或启用自动连接。

地址可以写成 `127.0.0.1:3001`、`ws://127.0.0.1:3001` 或 `wss://bot.example.com/onebot`。未写协议时自动使用 `ws://`。

## 常用命令

| 命令 | 说明 |
| --- | --- |
| `/mcbot connect [地址]` | 连接 OneBot 正向 WebSocket |
| `/mcbot disconnect` | 断开连接 |
| `/mcbot addGroup <群号>` | 添加互通群 |
| `/mcbot delGroup <群号>` | 删除互通群 |
| `/mcbot setBot <QQ号>` | 设置机器人 QQ |
| `/mcbot setAuthKey <令牌>` | 设置访问令牌 |
| `/mcbot status` | 查看连接和功能状态 |
| `/mcbot customs` | 列出自定义命令 |
| `/mcbot reload` | 重新加载配置 |

管理命令要求 2 级权限。自定义群命令存放在 `mcbot/cmds/`，简体中文为默认语言。

## 构建

使用 JDK 21：

```bash
./gradlew clean build
```

Fabric 和 NeoForge 的可安装产物分别位于 `fabric/build/libs/` 与 `neoforge/build/libs/`。每次推送和拉取请求都会由 GitHub Actions 自动测试、构建并上传产物；正式版本由仓库的统一发布流程生成。

## 反馈与许可

问题请提交到 [Minecraft0122/McBot-Reloaded Issues](https://github.com/Minecraft0122/McBot-Reloaded/issues)，并附上 Minecraft、Java、加载器、McBot 版本、完整日志和脱敏配置。

项目依据 [GNU GPL 3.0 或更高版本](LICENSE) 发布。感谢原作者 cnlimiter、Nova Committee 与所有历史贡献者。
