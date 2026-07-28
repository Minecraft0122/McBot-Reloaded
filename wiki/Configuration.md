# 配置参考

正式版本的主配置位于服务端工作目录下的 `mcbot/config.json`。首次启动会自动生成；推荐先让服务端正常生成文件，再修改其中的值。

> 不要删除顶层 `version` 字段。配置结构版本不匹配时，McBot 会备份旧文件并生成新配置。

## 修改方式

- 先停止服务端，编辑并保存 `mcbot/config.json`，然后启动。
- 服务端运行时修改后执行 `/mcbot reload`，重新加载配置、语言和自定义命令。
- Fabric 客户端/单人游戏可选安装 Mod Menu，通过配置界面修改。

## `common`：通用设置

| 字段 | 默认值 | 说明 |
| --- | --- | --- |
| `groupOn` | `true` | 启用 QQ 群互通 |
| `groupIdList` | `[]` | 允许互通的 QQ 群号列表，群号按字符串保存 |
| `enable` | `true` | McBot 全局服务开关 |
| `debug` | `false` | 调试日志；仅排障时临时启用 |
| `languageSelect` | `zh_cn` | 语言：`zh_cn`、`zh_tw`、`zh_hk`、`en_us` |
| `autoOpen` | `true` | 服务端启动后自动连接 OneBot |
| `imageOn` | `true` | 启用图片/CQ 图片相关处理 |
| `bindOn` | `false` | 启用玩家绑定相关功能 |

## `botConfig`：OneBot 连接

| 字段 | 默认值 | 说明 |
| --- | --- | --- |
| `tag` | `main` | 跨服与权限命名空间标识 |
| `url` | `127.0.0.1:18082` | OneBot 正向 WebSocket 地址，支持 `ws://` 和 `wss://` |
| `token` | 空 | OneBot 访问令牌 |
| `botId` | `0` | 机器人 QQ 号 |
| `reconnect` | `true` | 自动重连 |
| `reconnectMaxTimes` | `3` | 最大自动重连次数，允许 0—10 |
| `reconnectInterval` | `5` | 重连间隔参数，默认 5 |

`tag` 会参与绑定用户的命令权限名称，例如 `main.mcbot.cmd.list`。同一套绑定数据用于多组服务器时，应为不同服务器设置不同且稳定的 tag。

## `cmd`：消息格式与命令前缀

| 字段 | 默认值 | 说明 |
| --- | --- | --- |
| `welcomeNotice` | `欢迎加群~` | QQ 群成员加入提示 |
| `leaveNotice` | `离开了我们qwq` | QQ 群成员离开提示 |
| `cmdStart` | `!` | QQ 群自定义命令前缀 |
| `gamePrefixOn` | `true` | QQ → 游戏消息显示来源前缀 |
| `idGamePrefixOn` | `true` | 来源前缀包含 ID 信息 |
| `qqGamePrefix` | `群聊` | QQ 群消息来源名称 |
| `guildGamePrefix` | `频道` | 频道消息来源名称 |
| `groupNickOn` | `true` | 优先显示群名片/昵称 |
| `mcPrefixOn` | `true` | 游戏 → QQ 消息显示前缀 |
| `mcPrefix` | `MC` | 游戏消息前缀文字 |
| `mcChatPrefixOn` | `false` | 仅转发带指定前缀的游戏聊天 |
| `mcChatPrefix` | `q` | 游戏聊天触发前缀 |
| `qqChatPrefixOn` | `false` | 仅转发带指定前缀的 QQ 聊天 |
| `qqChatPrefix` | `m` | QQ 聊天触发前缀 |

启用聊天前缀过滤后，请先在测试群验证实际输入形式，以免误以为转发失效。

## `status`：接收开关

| 字段 | 默认值 | 说明 |
| --- | --- | --- |
| `connectInfoEnable` | `true` | 显示连接状态消息 |
| `rEnable` | `true` | 接收 QQ 消息总开关 |
| `rCmdEnable` | `true` | 接收 QQ 群命令 |
| `rChatEnable` | `true` | 接收 QQ 群聊天 |

## `status`：发送开关

| 字段 | 默认值 | 说明 |
| --- | --- | --- |
| `sEnable` | `true` | 向 QQ 发送消息总开关 |
| `sQqWelcomeEnable` | `true` | 转发 QQ 群成员加入事件 |
| `sQqLeaveEnable` | `true` | 转发 QQ 群成员离开事件 |
| `sJoinEnable` | `true` | 转发玩家加入 |
| `sLeaveEnable` | `true` | 转发玩家离开 |
| `sDeathEnable` | `true` | 转发玩家死亡 |
| `sChatEnable` | `true` | 转发玩家聊天 |
| `sAdvanceEnable` | `true` | 转发玩家进度/成就 |

可使用 `/mcbot receive ...` 和 `/mcbot send ...` 修改这些开关，完整语法见[命令参考](Commands)。

## 数据与备份

- `mcbot/cmds/*.json`：自定义群命令。
- `mcbot/data/userBind.csv`：QQ 与游戏角色绑定和权限。
- `mcbot/data/chatRecord.csv`：聊天记录索引。
- `mcbot/config_日期 时间.json`：配置结构不兼容时生成的备份。

备份时建议复制整个 `mcbot/` 目录。不要在服务端运行期间用电子表格程序独占打开 CSV 文件。

