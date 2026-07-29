# OneBot 配置

McBot 实现的是 **OneBot 11 正向 WebSocket 客户端**。因此 OneBot 端必须开启“WebSocket 服务端”或“正向 WebSocket”，由 McBot 主动连接它。不要把仅有反向 WebSocket 的地址填入 McBot。

## 推荐：NapCatQQ

NapCatQQ 当前官方文档将“WebSocket 服务端”定义为通常所说的正向 WS。详细界面和字段说明以 [NapCat 网络配置官方文档](https://napneko.github.io/config/basic)为准。

在 NapCat WebUI 中进入“网络配置”，新建 WebSocket 服务端，建议值如下：

| 项目 | 建议值 | 说明 |
| --- | --- | --- |
| 启用 | 是 | 保存后确认服务已启动 |
| 主机 | `127.0.0.1` | McBot 与 NapCat 同机时最安全 |
| 端口 | `18082` | 与 McBot 地址一致即可，也可自定义 |
| 消息格式 | `array` | McBot 3.0.2 同时兼容数组和字符串消息 |
| 上报自身消息 | 否 | 避免机器人自己的消息形成回环 |
| Token | 强随机字符串 | 必须与 McBot 的 `token` 完全一致 |
| 心跳 | 保持默认 | 用于发现失效连接 |

NapCat 官方兼容表确认正向 WebSocket 可用，详见 [OneBot API 与连接兼容情况](https://napneko.github.io/develop/api)。

## McBot 端对应配置

假设 NapCat 与 Minecraft 在同一台机器，WebSocket 服务监听 `127.0.0.1:18082`：

| McBot 字段 | 示例 |
| --- | --- |
| `botConfig.url` | `127.0.0.1:18082` 或 `ws://127.0.0.1:18082` |
| `botConfig.token` | 与 NapCat Token 完全一致 |
| `botConfig.botId` | 登录 NapCat 的机器人 QQ 号 |
| `common.groupIdList` | 需要互通的 QQ 群号列表 |
| `common.autoOpen` | `true`，服务端启动后自动连接 |

McBot 支持 `ws://`、`wss://` 以及不带协议的地址；不带协议时会自动补成 `ws://`。

## Docker 或跨机器部署

`127.0.0.1` 永远指向当前进程所在的机器或容器：

- Minecraft 和 NapCat 在不同容器时，使用 Docker 网络中的服务名和端口。
- Minecraft 在容器、NapCat 在宿主机时，使用平台提供的宿主机网关地址。
- 两台物理机器部署时，使用 NapCat 所在机器的内网地址，并只允许 Minecraft 服务器来源访问该端口。

跨机器部署时不要把无令牌的 `ws://0.0.0.0:端口` 直接暴露到公网。优先使用内网、VPN、防火墙白名单或受信任证书保护的 `wss://`。

## 连接顺序

推荐顺序：

1. 启动并登录 OneBot 实现。
2. 确认正向 WebSocket 服务正在监听。
3. 启动 Minecraft 服务端。
4. 执行 `/mcbot status` 检查状态。
5. 如未连接，执行 `/mcbot connect` 或 `/mcbot connect <地址>`。

McBot 3.0.2 的连接过程在后台线程执行；OneBot 暂时不可用不会再阻塞 Minecraft 服务端启动。

## Token 不一致

Token 不一致通常表现为 WebSocket 握手失败、403、连接后立即断开或无法调用 API。请确认：

- 两端没有多余空格；
- 大小写完全一致；
- 修改后已保存 OneBot 配置；
- McBot 已执行 `/mcbot reload` 或正常重启；
- 地址指向的是正向 WebSocket 服务，而不是 WebUI 或 HTTP API 端口。

出于安全原因，推荐在服务端本地编辑配置文件设置令牌。提交日志或配置到 Issue 前必须将令牌替换为 `<已脱敏>`。
