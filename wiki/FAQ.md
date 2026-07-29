# 常见问题

## 玩家客户端需要安装 McBot 吗？

专用服务器不需要。McBot 是服务端模组，普通玩家客户端无需安装。单人游戏或集成服务器由客户端进程承担服务端职责时，可以安装 McBot。

## Fabric 还需要安装 Fabric API 吗？

不需要。3.0.2 起，Fabric 版本使用原版 Mixin 转发生命周期和命令事件，配置核心与 OneBot Client 也已经内置。服务端只需 Fabric Loader 和对应版本的 McBot JAR；Mod Menu 仅是可选客户端界面。

## 可以把 1.20.1 JAR 用在 1.20.2 或 1.20.4 吗？

不可以。Minecraft 和加载器 API 存在二进制差异，必须使用明确匹配的构建。正式支持范围见[兼容版本](Compatibility)。

## 为什么 Release 里有多个 JAR？

文件名同时包含 Minecraft 版本和加载器。选择与你服务端完全一致的文件：

- `1.20.1-...-fabric.jar`
- `1.20.1-...-forge.jar`
- `1.21.1-...-fabric.jar`
- `1.21.1-...-neoforge.jar`

不能同时安装两个。

## Source code 压缩包能直接安装吗？

不能。它是 GitHub 自动生成的源码快照。请下载 Release Assets 中以加载器名称结尾的 JAR。

## 支持哪些 QQ 机器人框架？

McBot 面向 OneBot 11 正向 WebSocket。推荐使用仍在维护且明确支持正向 WebSocket 的实现，例如 NapCatQQ。具体实现的安装和登录问题不属于 McBot 本身。

## 正向 WebSocket 和反向 WebSocket有什么区别？

McBot 是客户端，它主动连接 OneBot 的 WebSocket 服务端，这在 OneBot 生态中通常称为正向 WebSocket。配置 OneBot 时应创建 WebSocket 服务端，而不是让 OneBot 反向连接 McBot。

## 可以连接远程 OneBot 吗？

可以。McBot 支持 `ws://` 和 `wss://`。跨机器部署必须设置 Token，并使用防火墙、专用网络、VPN 或有效 TLS，禁止把无鉴权端口直接暴露到公网。

## 修改配置必须重启服务器吗？

通常不需要。修改 `mcbot/config.json` 或 `mcbot/cmds/*.json` 后执行 `/mcbot reload`。涉及模组 JAR、加载器或 Java 版本时必须完整重启。

## 为什么 QQ 群命令没有响应？

确认命令前缀、`rEnable`、`rCmdEnable`、互通群号和自定义命令 `enable`。还要检查权限规则；只有 `permission: "ALL"`、群主/管理员、白名单 QQ 或具备绑定权限的用户可以执行。

## 自定义命令为什么必须写 `alies`？

这是历史数据结构保留的字段名。当前解析器读取 `alies`，改成常见拼写 `aliases` 会导致别名不生效。

## 可以让 QQ 群执行任意服务器命令吗？

技术上自定义命令会调用服务端命令源，因此必须严格限制。不要向所有人开放 `op`、`stop`、权限授予、文件管理、插件管理或能够间接执行任意命令的入口。

## 1.12.2 为什么没有下载？

该分支依赖的旧 OneBot SDK 上游制品已失效。项目不会用来源不明的 JAR 拼装正式版本，因此目前不提供可交付 1.12.2 二进制文件。

## 如何确认下载没有损坏？

下载 Release 中的 `SHA256SUMS.txt`，用系统 SHA-256 工具计算 JAR 摘要并对照。正式发布还包含 GitHub 生成的构建来源证明。

## 在哪里反馈？

使用[本项目 Issues](https://github.com/Minecraft0122/McBot-Reloaded/issues/new/choose)，不要在已经停止维护的上游仓库提交本分支问题。提交前阅读[故障排除](Troubleshooting)。
