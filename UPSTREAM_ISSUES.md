# 上游开放问题梳理

数据源：[Nova-Committee/McBot 开放 issue](https://github.com/Nova-Committee/McBot/issues)，最后核对时间：2026-07-23。

当前开发分支为 Minecraft 1.20.1 的 `multi/1.20.1`。下表中的“已处理”表示本分支已经包含对应修改；其他 Minecraft 版本的问题不会直接套用到本分支。

| Issue | 摘要 | 适用范围 | 本分支处理情况 |
| --- | --- | --- | --- |
| [#196](https://github.com/Nova-Committee/McBot/issues/196) | 模组物品触发进度时崩服 | Fabric 1.21.1 | 需要在 `multi/1.21` 修复 `Optional` 空值，不适用于 1.20.1 API |
| [#194](https://github.com/Nova-Committee/McBot/issues/194) | 连接 NapCat 时地址格式错误 | 1.19.2 | 旧分支问题；本分支会自动补全 `ws://` |
| [#193](https://github.com/Nova-Committee/McBot/issues/193) | 停服后仍重连，进程无法退出 | 1.19.2 | 已加固共享停服流程：先关闭 OneBot、等待数据任务、保存数据并关闭线程池 |
| [#192](https://github.com/Nova-Committee/McBot/issues/192) | 1.7.10 无法正常使用 | 1.7.10 | 需要在 `1.7.10-forge` 分支处理 |
| [#191](https://github.com/Nova-Committee/McBot/issues/191) | 请求支持 1.21.5 及更新版本 | 1.21.5+ | 新版本适配，不属于本分支 |
| [#190](https://github.com/Nova-Committee/McBot/issues/190) | 玩家进度空 `Optional` 导致崩服 | 1.21.1 | 与 #196 同类，需要在 `multi/1.21` 处理 |
| [#189](https://github.com/Nova-Committee/McBot/issues/189) | 原文档链接失效 | 文档 | 已重写仓库内中文 README，不再依赖失效站点 |
| [#188](https://github.com/Nova-Committee/McBot/issues/188) | 1.7.10 无法正常使用 | 1.7.10 | 需要在 `1.7.10-forge` 分支处理 |
| [#187](https://github.com/Nova-Committee/McBot/issues/187) | 玩家进度空 `Optional` 导致崩服 | 1.21.1 | 与 #196 同类，需要在 `multi/1.21` 处理 |
| [#186](https://github.com/Nova-Committee/McBot/issues/186) | `say` 等命令返回空消息导致 OneBot 报错 | 1.20.1/1.21.1 | 已处理：所有群消息发送入口都会忽略空内容 |
| [#185](https://github.com/Nova-Committee/McBot/issues/185) | `/mcbot delGroup` 失效 | 1.20.1/1.21.1 | 已处理：统一 Brigadier 参数名并增加成功提示 |
| [#184](https://github.com/Nova-Committee/McBot/issues/184) | 自定义命令回复丢失换行 | 1.20.1/1.21.1 | 已处理：命令返回的多段消息之间保留换行 |
| [#183](https://github.com/Nova-Committee/McBot/issues/183) | `@` 消息显示 `@[null]` | 共享逻辑 | 已处理：名称缺失时回退到 QQ 号 |
| [#182](https://github.com/Nova-Committee/McBot/issues/182) | 群名片功能失效 | 1.20.1/1.21.1 | 已处理：优先群名片，空名片回退到昵称和 QQ 号 |
| [#175](https://github.com/Nova-Committee/McBot/issues/175) | 自定义命令回复显示消息类型不支持 | 1.20.1 | 已补充空消息过滤；消息数组解析已包含上游 #181 的修复 |
| [#170](https://github.com/Nova-Committee/McBot/issues/170) | 1.7.10 无法使用 | 1.7.10 | 需要在 `1.7.10-forge` 分支处理 |
| [#159](https://github.com/Nova-Committee/McBot/issues/159) | 将复杂命令输出转为图片 | 功能请求 | 尚未实现；需要确定字体、渲染和图片上传方案 |
| [#131](https://github.com/Nova-Committee/McBot/issues/131) | 1.16.5 Forge 无法加载 | 1.16.5 | 需要在对应旧版本分支处理 |
| [#115](https://github.com/Nova-Committee/McBot/issues/115) | Arclight/TabTPS 命令无返回 | 1.19.2，异步命令 | 依赖命令异步输出，现有同步命令源无法完整捕获 |
| [#112](https://github.com/Nova-Committee/McBot/issues/112) | 1.12.2 无法加载 | 1.12.2 | 需要在 `1.12.2-forge` 分支处理 |
| [#79](https://github.com/Nova-Committee/McBot/issues/79) | Spark TPS 异步结果为空 | 旧版本，异步命令 | 与 #115 同类，需要专门的异步输出捕获机制 |
| [#77](https://github.com/Nova-Committee/McBot/issues/77) | 长期功能建议汇总 | 长期计划 | 按具体需求分别评估，不作为单一缺陷修复 |

## 本轮采用的修复范围

本轮优先处理可在 1.20.1 共用代码中静态确认、能够通过单元测试或构建验证的问题。1.7.10、1.12.2、1.16.5、1.19.2 和 1.21.x 的专属问题保留在各自分支中处理，以免引入跨版本 API 不兼容。
