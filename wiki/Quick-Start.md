# 快速开始

本页用于完成最小可用部署。遇到连接问题时，请继续阅读 [OneBot 配置](OneBot-Setup)和[故障排除](Troubleshooting)。

## 准备内容

- 一个受支持的 Minecraft 服务端。
- 与 Minecraft 版本和加载器匹配的 McBot JAR。
- 对应 Minecraft 版本和加载器的 McBot JAR；所有正式加载器均不需要其他前置模组。
- 一个支持 OneBot 11 正向 WebSocket 的 QQ 机器人实现，推荐使用仍在维护的 [NapCatQQ](https://github.com/NapNeko/NapCatQQ)。
- 机器人 QQ 号、需要互通的 QQ 群号，以及自定义访问令牌。

## 五分钟部署

1. 从 [GitHub Releases](https://github.com/Minecraft0122/McBot-Reloaded/releases/latest)下载正确的 McBot JAR，不要下载 GitHub 自动生成的 `Source code` 压缩包。
2. 把 JAR 放入服务端的 `mods` 目录，不需要安装 Fabric API 或 Jupiter。
3. 启动一次服务端，等待生成 `mcbot/config.json` 和 `mcbot/cmds/`，然后正常停止服务端。
4. 在 OneBot 实现中建立“WebSocket 服务端/正向 WebSocket”，建议监听 `127.0.0.1:18082`，设置一个强随机令牌，并关闭自身消息上报。
5. 修改 `mcbot/config.json`，至少设置 WebSocket 地址、访问令牌、机器人 QQ 号和互通群号。不要删除配置中的 `version` 字段。
6. 启动 OneBot 和 Minecraft 服务端。默认启用自动连接，也可以在控制台执行 `/mcbot connect`。

## 首次验证

在服务端控制台执行：

```text
/mcbot status
```

确认输出中至少包含：

- WebSocket 连接为“已开启”；
- 机器人 QQ 号正确；
- 互通群号包含目标群；
- 全局服务、接收消息和发送消息均已开启。

随后进行双向测试：

1. 在目标 QQ 群发送普通文字，确认游戏内能看到消息。
2. 在游戏内发送普通聊天，确认目标 QQ 群能收到消息。
3. 在 QQ 群发送 `!list`，确认能够获得在线玩家列表。

## 常用管理命令

```text
/mcbot addGroup <群号>
/mcbot delGroup <群号>
/mcbot setBot <机器人QQ号>
/mcbot connect [ws://主机:端口]
/mcbot disconnect
/mcbot reload
/mcbot status
```

这些命令要求 Minecraft 2 级权限。完整说明见[命令参考](Commands)。

## 安全提醒

- 不要把 OneBot WebSocket 无鉴权暴露到公网。
- 不要在 Issue、聊天截图或公开日志中提交真实令牌。
- 同机部署时优先监听 `127.0.0.1`；跨机器部署时优先使用防火墙、专用网络或 `wss://`。
- 正式服务器建议先在测试群完成双向消息和命令权限验证。
