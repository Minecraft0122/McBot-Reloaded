# 故障排除

先执行：

```text
/mcbot status
```

并保留从 McBot 初始化开始到问题发生后的完整服务端日志。不要只截取最后一行异常。

## 无法连接 OneBot

逐项确认：

1. OneBot 已登录机器人 QQ，正向 WebSocket 服务已启用。
2. McBot 填写的是 WebSocket 服务端口，不是 WebUI、HTTP API 或反向 WebSocket 地址。
3. 地址支持 `ws://`、`wss://` 或 `主机:端口`；不支持 `http://`。
4. 两端 Token 完全一致，且没有首尾空格。
5. 防火墙允许 Minecraft 服务器访问该端口。
6. Docker 环境没有错误使用容器自身的 `127.0.0.1`。
7. 修改后已执行 `/mcbot reload` 或正常重启。

可用新地址直接测试：

```text
/mcbot disconnect
/mcbot connect 127.0.0.1:18082
/mcbot status
```

3.0.1 的连接过程不会阻塞 Minecraft 服务端主线程；OneBot 未及时启动时，Minecraft 应继续完成启动。

## QQ 能发到游戏，但游戏不能发到 QQ

- 确认 `/mcbot status` 中“发送消息”和“发送玩家聊天消息”均已开启。
- 执行 `/mcbot send all true` 和 `/mcbot send chat true`。
- 确认目标群号出现在互通群列表中。
- 如果启用了 `mcChatPrefixOn`，游戏消息必须使用配置的 `mcChatPrefix`。
- 检查 OneBot 机器人是否仍在目标群、是否被禁言、是否有发送消息失败日志。
- 局域网/单人世界仍属于需要实机回归的组合；反馈时注明是专用服务端还是集成服务器。

## 游戏能发到 QQ，但 QQ 不能发到游戏

- 确认 `/mcbot receive all true` 和 `/mcbot receive chat true`。
- 确认 `common.groupOn=true`，且来源群在 `groupIdList` 中。
- 如果启用了 `qqChatPrefixOn`，QQ 消息必须使用配置的 `qqChatPrefix`。
- NapCat 建议使用消息段数组；McBot 3.0.1 同时兼容数组和字符串消息。
- 关闭 OneBot 的自身消息上报，避免机器人消息回环。

## 消息重复转发

3.0.1 已增加群号去重和事件注册幂等保护。仍然重复时：

- 确认 `mods` 中只有一个 McBot JAR；
- 检查 `groupIdList` 是否包含重复群号；
- 确认没有同时运行两套连接到同一 OneBot 的 McBot 实例；
- 提供 Minecraft、加载器、McBot 和其他聊天类模组版本。

1.18.2/1.19.2 Fabric 与 Forge 的重复转发修复已回移并通过构建，但仍建议在实际模组包中回归。

## `/mcbot delGroup`、`setBot` 或自定义 `say` 报错

这些问题已在 3.0.1 修复：

- `delGroup` 统一使用 Long 群号参数并立即保存；
- `setBot` 修复数字类型解析并立即保存；
- `say` 支持最后一个 `%` 接收包含空格的剩余参数。

若仍能复现，先确认实际加载的是 3.0.1 JAR，而不是旧 JAR、开发包或 GitHub Source code。

## `/mcbot reload` 后没有变化

3.0.1 会重新加载主配置、语言和自定义命令。检查：

- JSON 语法有效且为 UTF-8；
- 自定义命令的 `enable` 为 `true`；
- 字段名必须是历史拼写 `alies`；
- 执行 `/mcbot customs` 确认命令已加载；
- 查看日志中“重新加载配置失败”或具体文件解析异常。

## 颜色码、`@null` 和换行

3.0.1 已处理：

- 群名片/昵称为空时回退到 QQ 号；
- 移除 Minecraft 旧式、十六进制和 ANSI 格式控制码；
- 保留命令结果换行；
- 接受 OneBot 字符串和消息段数组。

如果特定模组仍产生乱码，请提供未删改的原始日志和模组列表，不要只复制 QQ 中处理后的文本。

## 图片与复杂命令输出

- Chat Image Display 的图片占位路径已修复，但指定版本组合仍需真实客户端联调。
- Spark 等复杂命令的纯文本返回、换行和分段已经改善。
- 把任意复杂输出渲染成图片仍未实现，不能承诺所有组件、悬浮文本或图片均可还原。

## 1.7.10、1.12.2 和 GTNH

- 1.7.10 已完成 Java 8 构建，并回移连接、WSS、数组消息、热重载和重复监听修复；GTNH/真实服务端仍待回归。
- 1.12.2 依赖的旧 OneBot SDK 0.1.4 制品已经失效，当前没有可信的正式 JAR。
- 不要从不明网盘下载并替换缺失依赖，这会引入供应链和许可证风险。

## MC 百科已知问题状态

项目已逐项复核 [MC 百科《通用故障排除 - 群服互联》](https://www.mcmod.cn/post/5072.html)中的 20 项问题。完整状态表位于主仓库的 [MCMOD_KNOWN_ISSUES.md](https://github.com/Minecraft0122/McBot-Reloaded/blob/1.20.1/MCMOD_KNOWN_ISSUES.md)。

当前不能宣称全部修复：1.12.2 可交付构建和复杂输出图片化明确未完成；局域网、Chat Image Display、GTNH/Carpet 和部分旧版组合仍需要实机回归。

## 提交有效问题报告

通过[问题模板](https://github.com/Minecraft0122/McBot-Reloaded/issues/new/choose)提交，并附上：

- Minecraft、Java、加载器和 McBot 版本；
- OneBot 实现和版本；
- 完整日志文件；
- 最小复现步骤；
- 已脱敏的 `mcbot/config.json`；
- 是否安装聊天、假人、权限、跨服或图片显示类模组。

必须删除 QQ Token、公网地址中的凭据、Cookie、扫码登录信息和其他隐私数据。

