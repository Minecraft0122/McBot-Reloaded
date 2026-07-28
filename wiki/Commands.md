# 命令参考

Minecraft 端的所有 McBot 管理命令都以 `/mcbot` 开头，并要求 2 级权限。可从服务端控制台执行，也可由拥有相应权限的玩家执行。

## 连接与状态

| 命令 | 说明 |
| --- | --- |
| `/mcbot help` | 显示内置帮助和问题反馈链接 |
| `/mcbot status` | 显示连接、群号、收发开关和白名单状态 |
| `/mcbot connect` | 使用当前配置中的地址建立连接 |
| `/mcbot connect <地址>` | 保存指定地址并异步连接；支持 `ws://`、`wss://` 或无协议地址 |
| `/mcbot disconnect` | 主动断开当前 WebSocket 连接 |
| `/mcbot reload` | 重新加载主配置、语言和 `mcbot/cmds/` 中的自定义命令 |

示例：

```text
/mcbot connect 127.0.0.1:18082
/mcbot connect wss://bot.example.com/onebot
```

## 机器人和群号

| 命令 | 说明 |
| --- | --- |
| `/mcbot setBot <QQ号>` | 设置机器人 QQ 号并保存配置 |
| `/mcbot setAuthKey <令牌>` | 设置 OneBot Token |
| `/mcbot addGroup <群号>` | 添加互通 QQ 群并保存配置 |
| `/mcbot delGroup <群号>` | 删除互通 QQ 群并保存配置 |
| `/mcbot debug <true\|false>` | 开关调试模式 |

令牌属于敏感信息。优先在服务端本地配置文件中修改，不要在玩家可见环境、直播画面或公开日志中输入真实令牌。

## QQ → Minecraft 接收开关

| 命令 | 说明 |
| --- | --- |
| `/mcbot receive all <true\|false>` | QQ 消息接收总开关 |
| `/mcbot receive chat <true\|false>` | 接收普通群聊；设为 true 时也会打开总开关 |
| `/mcbot receive cmd <true\|false>` | 接收群命令；设为 true 时也会打开总开关 |

## Minecraft → QQ 发送开关

| 命令 | 说明 |
| --- | --- |
| `/mcbot send all <true\|false>` | 向 QQ 发送消息总开关 |
| `/mcbot send join <true\|false>` | 玩家加入消息 |
| `/mcbot send leave <true\|false>` | 玩家离开消息 |
| `/mcbot send death <true\|false>` | 玩家死亡消息 |
| `/mcbot send chat <true\|false>` | 玩家聊天消息 |
| `/mcbot send achievements <true\|false>` | 玩家进度/成就消息 |
| `/mcbot send qqWelcome <true\|false>` | QQ 群成员加入消息 |
| `/mcbot send qqLeave <true\|false>` | QQ 群成员离开消息 |

打开任一单项发送开关时，总发送开关也会自动打开；关闭某个单项不会自动关闭总开关。

## 玩家绑定

| 命令 | 说明 |
| --- | --- |
| `/mcbot addBind <群号> <QQ号> <游戏名>` | 建立群内 QQ 账号与 Minecraft 名称的绑定 |
| `/mcbot delBind <群号> <QQ号>` | 删除绑定 |

绑定数据保存在 `mcbot/data/userBind.csv`。默认 `bind.json` 和 `unbind.json` 允许用户从 QQ 群发起绑定/解绑命令。

## 自定义命令

| 命令 | 说明 |
| --- | --- |
| `/mcbot customs` | 列出当前已加载的自定义命令 ID |

QQ群内默认使用 `!` 前缀，例如：

```text
!list
!服务器在线
!say 服务器将在十分钟后维护
```

前缀可通过 `cmd.cmdStart` 修改。自定义命令的 JSON 字段、变量和权限说明见[自定义群命令](Custom-Commands)。

## 常见操作组合

临时停止转发聊天，但保留命令：

```text
/mcbot receive chat false
/mcbot send chat false
```

更换 OneBot 地址：

```text
/mcbot disconnect
/mcbot connect 192.168.1.20:18082
/mcbot status
```

修改配置或命令文件后：

```text
/mcbot reload
/mcbot customs
/mcbot status
```

