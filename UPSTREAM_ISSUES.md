# 上游开放问题梳理

数据源：[Nova-Committee/McBot 开放 issue](https://github.com/Nova-Committee/McBot/issues)，最后核对：2026-07-28。共逐项复核 22 个开放问题；“已修复”只表示指定版本分支已有代码和构建验证，不替上游仓库关闭 issue。

| Issue | 适用范围 | 3.0.0 处理状态 |
| --- | --- | --- |
| [#196](https://github.com/Nova-Committee/McBot/issues/196) | Fabric 1.21.1，模组物品进度崩服 | **已修复**：安全处理没有展示信息的 `Advancement.display()` |
| [#194](https://github.com/Nova-Committee/McBot/issues/194) | 1.19.2 NapCat 地址 | **已修复并构建**：吸收 #197，正式分支及 Fabric/Forge 1.19.2 均支持 `ws://`、`wss://` 和无协议地址；两种加载器的 GitHub Actions 构建通过 |
| [#193](https://github.com/Nova-Committee/McBot/issues/193) | 1.19.2 停服后仍重连 | **已修复并构建**：停服会终止重连与消息线程，连接失败不会杀死保活线程；Fabric/Forge 1.19.2 的 GitHub Actions 构建通过 |
| [#192](https://github.com/Nova-Committee/McBot/issues/192) | 1.7.10 数组消息无法解析 | **已修复并构建**：旧版事件模型同时接受 OneBot 字符串和消息段数组 |
| [#191](https://github.com/Nova-Committee/McBot/issues/191) | 请求 1.21.5/1.21.7+ | **尚未完成**：3.0.0 正式支持到 1.21.1；新版聊天与网络 API 需要独立移植，不能只改 `fabric.mod.json` |
| [#190](https://github.com/Nova-Committee/McBot/issues/190) | 1.21.1 进度空值崩服 | **已修复**：与 #196 同一根因 |
| [#189](https://github.com/Nova-Committee/McBot/issues/189) | 文档链接失效 | **已修复**：仓库内提供完整中文安装、兼容与发布文档 |
| [#188](https://github.com/Nova-Committee/McBot/issues/188) | 1.7.10 无法正常使用 | **部分完成**：Java 8 本地与 GitHub Actions 干净构建已通过并修复 #192/#170；GTNH 的 Java 21 组合仍需真实服务器回归 |
| [#187](https://github.com/Nova-Committee/McBot/issues/187) | 1.21.1 进度空值崩服 | **已修复**：与 #190/#196 同一根因 |
| [#186](https://github.com/Nova-Committee/McBot/issues/186) | 空命令回复被 OneBot 拒绝 | **已修复**：所有群消息入口过滤空内容 |
| [#185](https://github.com/Nova-Committee/McBot/issues/185) | `/mcbot delGroup` 失效 | **已修复**：统一 Brigadier 参数名并返回成功提示 |
| [#184](https://github.com/Nova-Committee/McBot/issues/184) | NeoForge 命令输出丢换行 | **已修复**：多段命令反馈以换行拼接 |
| [#183](https://github.com/Nova-Committee/McBot/issues/183) | `@` 显示 `@[null]` | **已修复**：名称缺失时回退到 QQ 号 |
| [#182](https://github.com/Nova-Committee/McBot/issues/182) | 群名片开关失效 | **已修复**：优先群名片，空值回退昵称和 QQ 号 |
| [#175](https://github.com/Nova-Committee/McBot/issues/175) | 自定义命令消息类型不支持 | **已修复主因**：兼容 OneBot 数组消息并过滤空回复；仍欢迎提供 Mohist 特有复现日志 |
| [#170](https://github.com/Nova-Committee/McBot/issues/170) | 1.7.10 `addGroup` 空指针 | **已修复并构建**：将误用的 `Long.getLong` 改为严格十进制解析和参数校验 |
| [#159](https://github.com/Nova-Committee/McBot/issues/159) | Spark 复杂输出转图片 | **功能请求，未完成**：图片渲染需要字体、分页和 OneBot 上传方案；先处理异步文本捕获与安全分段 |
| [#131](https://github.com/Nova-Committee/McBot/issues/131) | Forge 1.16.5 缺少 OneBot `FileUtils` | **已修复并构建**：改用官方 OneBot Client 0.4.3，并将 OneBot 与可验证来源的 AtomConfig 打入最终 JAR；本地及 GitHub Actions 完整构建通过 |
| [#115](https://github.com/Nova-Committee/McBot/issues/115) | Arclight 1.19.2 TPS 无返回 | **已修复主因并构建**：命令输出改为有界异步捕获并隔离每次响应；混合端特有差异仍需用完整日志回归 |
| [#112](https://github.com/Nova-Committee/McBot/issues/112) | Forge 1.12.2 无法加载 | **已定位外部阻断**：ForgeGradle/Minecraft 映射可正常生成，但上游 `OneBot-SDK:0.1.4` 制品已从公开 Maven 仓库移除并返回 404；在取得可验证原始制品或完成 Java 8 客户端替换前不发布不完整 JAR |
| [#79](https://github.com/Nova-Committee/McBot/issues/79) | Spark TPS 异步结果为空 | **已修复主因并构建**：加入有界等待、响应隔离和安全文本分段，避免异步结果为空或串到下一条命令 |
| [#77](https://github.com/Nova-Committee/McBot/issues/77) | 长期功能建议 | **持续推进**：语言、绑定、记录、欢迎 `@` 已有实现；KOOK、Koishi/NoneBot2 等属于独立后端项目，不冒充缺陷修复 |

未标记“已修复”的项目不会被写入正式发行说明为已完成。需要外部服务、特定混合端或缺失日志的问题，会保留明确的验证条件。
