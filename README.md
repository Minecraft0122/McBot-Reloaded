# McBot Reloaded

McBot Reloaded 是一个基于 [OneBot 11](https://github.com/botuniverse/onebot-11) 协议的 Minecraft 服务端模组，用于在 Minecraft 服务器与 QQ 群之间转发消息，并允许群成员查询服务器状态或执行获准的自定义命令。

完整的安装、OneBot 接入、配置、命令和故障排除说明见[中文 Wiki](https://github.com/Minecraft0122/McBot-Reloaded/wiki)；对应的版本控制源文件位于仓库的 [`wiki/`](wiki/) 目录，并由工作流自动同步。

本项目由 **Minecraft0122** 继续维护；本分支面向 Minecraft 1.20.1，同时支持 Fabric 与 Forge。代码源自 [Nova-Committee/McBot](https://github.com/Nova-Committee/McBot)，原作者与历史贡献者署名依照 GPL-3.0-or-later 保留。

## 下载

请从 [GitHub Releases 最新版本](https://github.com/Minecraft0122/McBot-Reloaded/releases/latest)下载，不要使用页面上由 GitHub 自动生成的 `Source code` 压缩包。

- Fabric 服务端下载文件名以 `-fabric.jar` 结尾；不需要安装 Fabric API 或其他前置模组。
- Forge 服务端下载文件名以 `-forge.jar` 结尾，不要与 Fabric 版本同时安装。
- 可使用同一 Release 中的 `SHA256SUMS.txt` 核对文件完整性。

将对应 JAR 放入服务端的 `mods` 目录即可。配置核心和 OneBot Client 已内置，普通玩家客户端无需安装 McBot。

## 功能

- 在 QQ 群与 Minecraft 服务器之间双向转发聊天消息。
- 转发玩家加入、离开、死亡和取得进度等服务器事件。
- 支持群名片、消息前缀、图片占位和多个互通群。
- 支持自定义群命令、权限控制、玩家绑定和聊天记录。
- 提供游戏内配置界面与简体中文配置说明。

## 兼容性

| 项目 | 支持范围 | 项目构建基准 |
| --- | --- | --- |
| Minecraft | **仅 1.20.1** | 1.20.1 |
| Java | 17、21、25 | 字节码目标为 Java 17；CI 使用 Java 21、25 启动真实服务端 |
| Fabric Loader | 0.19.3 或更高版本 | 0.19.3 |
| Forge | 47.4.22 至 47.x | 47.4.22 |
| OneBot | OneBot 11 正向 WebSocket | OneBot Client 0.4.3 |

本分支没有声明兼容 Minecraft 1.20.2—1.20.6 或 1.21.x；这些版本存在 Minecraft API 和加载器二进制差异，不能直接使用本分支产物。其他 Minecraft 版本的维护等级、加载器与 Java 要求见 [支持版本表](SUPPORTED_VERSIONS.md)，不同版本的 JAR 不能混用。配置核心和 OneBot Client 已打包进模组，Fabric、Forge 均不需要额外前置模组；Mod Menu 7.2.2 或更高版本仅用于可选的客户端配置界面。

Java 17 是 1.20.1 的最低运行版本和构建基准。Java 21、25 会在每次正式分支变更时分别启动 Fabric、Forge 专用服务器，确认 McBot 已加载后执行正常关服；Java 18—20、22—24 没有纳入持续兼容矩阵，不作同等级保证。使用 Java 25 时必须安装表中的最低加载器版本，旧版加载器可能在 McBot 初始化前就无法读取 Java 25 类文件。

这是服务端模组：专用服务器的普通玩家客户端无需安装。若在客户端或单人游戏中安装，模组仍可加载，并可通过 Mod Menu 使用配置界面。

OneBot 端可使用支持正向 WebSocket 的 OneBot 11 实现，例如 [NapCatQQ](https://github.com/NapNeko/NapCatQQ) 或 [Lagrange.Core](https://github.com/LagrangeDev/Lagrange.Core)。具体实现版本更新独立于本模组，因此以 OneBot 11 协议兼容性为准。

## 安装与连接

1. 安装与服务端加载器匹配的 McBot；不要混用其他 Minecraft 版本或加载器的 JAR。
2. 启动一次服务器，使模组生成 `mcbot/config.json` 和 `mcbot/cmds/`。
3. 在 OneBot 实现中启用正向 WebSocket，并记下地址、端口和访问令牌。
4. 修改 `mcbot/config.json` 中的机器人地址、QQ 号、令牌和互通群号，或在游戏内使用 `/mcbot` 命令配置。
5. 执行 `/mcbot connect`。若未启用自动连接，也可执行 `/mcbot connect <主机:端口>`。

默认 WebSocket 地址为 `127.0.0.1:18082`。地址可带或不带 `ws://` 前缀，也支持 `wss://` 加密连接；模组会自动补全普通 WebSocket 协议。

## 常用命令

| 命令 | 说明 |
| --- | --- |
| `/mcbot connect [主机:端口]` | 连接机器人框架 |
| `/mcbot disconnect` | 断开 WebSocket 连接 |
| `/mcbot addGroup <群号>` | 添加互通 QQ 群 |
| `/mcbot delGroup <群号>` | 删除互通 QQ 群 |
| `/mcbot setBot <QQ号>` | 设置机器人 QQ 号 |
| `/mcbot setAuthKey <令牌>` | 设置访问令牌 |
| `/mcbot receive <all\|chat\|cmd> <true\|false>` | 控制接收内容 |
| `/mcbot send <类型> <true\|false>` | 控制事件转发 |
| `/mcbot status` | 查看当前连接与功能状态 |
| `/mcbot customs` | 列出已加载的自定义命令 |
| `/mcbot reload` | 重新加载配置 |

所有管理命令都要求 2 级权限（管理员或命令方块级别）。

## 自定义群命令

自定义命令存放在 `mcbot/cmds/`。首次启动会生成 `list.json`、`say.json`、`bind.json` 和 `unbind.json` 示例。可用变量包括：

- `%group_id%`：来源群号
- `%user_id%`：发送者 QQ 号
- `%user_age%`：发送者年龄（由 OneBot 提供）
- `%user_nickname%`：发送者昵称
- 单独的 `%`：按顺序替换为群命令参数

修改自定义命令或配置后执行 `/mcbot reload` 即可重新加载，无需重启服务器。

## 中文支持

简体中文是默认语言。`mcbot/config.json` 中的 `languageSelect` 可设为 `zh_cn`、`zh_tw`、`zh_hk` 或 `en_us`。游戏内配置界面的简体中文文本位于 `common/src/main/resources/assets/mcbot/lang/zh_cn.json`，事件和死亡消息文本位于 `common/src/main/resources/lang/zh_cn.json`。

## 构建

本分支使用 JDK 17 驱动 Gradle 并生成 Java 17 字节码：

```bash
./gradlew clean build
```

Windows 可运行 `gradlew.bat clean build`。构建产物分别位于 `fabric/build/libs/` 和 `forge/build/libs/`。Java 21、25 是服务端运行兼容目标，不应直接替代本分支的 Gradle 基准 JDK；GitHub Actions 会把构建 JDK 与服务端运行 JDK 分离验证。

## 自动发布

向 GitHub 推送与 `gradle.properties` 中 `mod_version` 完全一致、没有 `v` 前缀的标签即可自动发布。例如 `mod_version=3.0.4` 时：

```bash
git tag 3.0.4
git push origin 3.0.4
```

自动发布会在干净环境中运行全部测试，从 `1.20.1` 分支构建 Fabric/Forge、从 `1.21.1` 分支构建 Fabric/NeoForge，共上传四个正式 JAR，并生成 `SHA256SUMS.txt` 和 GitHub 构建来源证明，然后创建带自动发行说明的 GitHub Release。标签与模组版本不一致时会拒绝发布；开发包和源码包不会作为 Release 附件上传。

## 反馈问题

提交问题前，请附上 Minecraft、Java、加载器和 McBot 版本，并提供完整日志、复现步骤及已脱敏的配置文件：

- [本项目问题列表](https://github.com/Minecraft0122/McBot-Reloaded/issues)
- [上游问题列表](https://github.com/Nova-Committee/McBot/issues)
- [上游开放问题梳理](UPSTREAM_ISSUES.md)
- [上游拉取请求梳理](UPSTREAM_PULLS.md)
- [MC 百科已知问题复核](MCMOD_KNOWN_ISSUES.md)
- [全部支持版本](SUPPORTED_VERSIONS.md)

## 致谢与许可

感谢 McBot 原作者与所有贡献者，以及 OneBot、Fabric、Forge、Architectury 和 Jupiter 等项目。

本项目依据 [GNU GPL 3.0 或更高版本](LICENSE)发布。
