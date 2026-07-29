# McBot Reloaded Wiki

McBot Reloaded 是一个基于 [OneBot 11](https://github.com/botuniverse/onebot-11) 的 Minecraft 服务端模组，用于在 Minecraft 服务器与 QQ 群之间双向转发消息，并允许群成员执行经过授权的服务器命令。

本项目由 **Minecraft0122** 维护，代码源自 Nova Committee 的 McBot。项目继续遵循 GPL-3.0-or-later，原作者、历史贡献者和 Git 历史均予保留。

## 从这里开始

1. 查看[兼容版本](Compatibility)，确认 Minecraft、加载器和 Java 版本。
2. 从[最新正式版](https://github.com/Minecraft0122/McBot-Reloaded/releases/latest)下载名称与服务端完全匹配的 JAR。
3. 按照[安装教程](Installation)安装与服务端版本匹配的单个 McBot JAR。
4. 按照[OneBot 配置](OneBot-Setup)启用正向 WebSocket。
5. 完成[快速开始](Quick-Start)中的首次连接测试。

## 正式支持

| Minecraft | 加载器 | 已验证 Java | 普通玩家客户端 |
| --- | --- | --- | --- |
| 1.20.1 | Fabric、Forge | 17、21、25 | 无需安装 |
| 1.21.1 | Fabric、NeoForge | 21、25 | 无需安装 |

Fabric 版本已经内置配置核心并改用原版 Mixin 事件，不需要 Fabric API、Jupiter 或其他前置模组。不同 Minecraft 版本和加载器的 JAR 不能混用。

Java 21、25 会在 GitHub Actions 中分别启动所有四种正式服务端组合，确认 McBot 已加载后正常关服。1.20.1 仍以 Java 17 为最低版本和编译目标，1.21.1 仍以 Java 21 为最低版本和编译目标。

## 主要功能

- QQ 群与 Minecraft 服务器双向聊天转发。
- 转发玩家加入、离开、死亡和进度事件。
- 多个 QQ 群互通、群名片、聊天前缀和图片占位支持。
- 可配置群命令、权限、玩家绑定和聊天记录。
- 简体中文、繁体中文和英文语言资源。
- 自动测试、构建、生成 SHA-256 校验文件和 GitHub Release。

## 当前状态

截至 2026-07-29，当前正式版本为 **3.0.3**。MC 百科故障排除页面列出的主要连接、命令和消息问题已经修复，但不能宣称全部问题均已解决：1.12.2 仍受失效上游依赖阻塞，复杂命令输出图片化尚未完成，部分模组组合仍需实机回归。详情见[已知问题与故障排除](Troubleshooting)。

## 获取帮助

- [常见问题](FAQ)
- [命令参考](Commands)
- [配置参考](Configuration)
- [提交问题](https://github.com/Minecraft0122/McBot-Reloaded/issues/new/choose)
- [项目仓库](https://github.com/Minecraft0122/McBot-Reloaded)
