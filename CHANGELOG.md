# 更新日志

本文件记录 McBot 的重要变更。历史内容整理自上游提交记录。

## 未发布

## [3.0.3](https://github.com/Minecraft0122/McBot-Reloaded/releases/tag/3.0.3) - 2026-07-29

- 将 Java 25 运行矩阵使用的最低加载器更新为 Fabric Loader 0.19.3 和 Forge 47.4.22，避免旧版 ASM/Mixin 无法读取 Java 25 类文件。
- 首次启动缺少 `mcbot/config.json` 时直接生成默认配置，不再先记录一次配置读取异常。
- 为 1.20.1 Fabric/Forge 与 1.21.1 Fabric/NeoForge 增加 Java 21、25 真实服务端启动、McBot 加载和正常关服测试。
- 构建继续使用各 Minecraft 版本的基准 JDK，服务端运行进程使用独立 JDK，避免把 Gradle 兼容性误判为模组运行兼容性。
- 启动日志记录 Java 版本、供应商和虚拟机，方便定位客户环境问题。
- 同步更新 README、支持版本表与中文 Wiki 的 Java 兼容矩阵。

## [3.0.2](https://github.com/Minecraft0122/McBot-Reloaded/releases/tag/3.0.2) - 2026-07-29

### 独立运行

- 移除 Fabric API 必需依赖，改用原版 Mixin 转发生命周期、命令、玩家登录退出和刻末事件。
- 将 Jupiter 缩减为内置配置核心并重定位命名空间；Fabric 用户不再需要安装 Fabric API、Jupiter 或其他前置模组。
- 修复 OneBot 自动重连计时器在关服后残留、导致 JVM 无法退出的问题。
- Minecraft 1.20.1 与 1.21.1 Fabric 均已在仅安装 Fabric Loader 和 McBot 的空白服务端完成启动、生成配置、优雅关服回归。

## [3.0.1](https://github.com/Minecraft0122/McBot-Reloaded/releases/tag/3.0.1) - 2026-07-28

### 修复

- 修复 `/mcbot setBot` 的 Brigadier 参数类型不匹配，并让机器人账号、群号与连接地址修改立即保存。
- `/mcbot connect` 现在接受 `ws://`、`wss://` 和无协议地址，后台建立连接，不再因网络超时阻塞服务器启动或命令线程。
- 修复旧版 Fabric/Forge 的 `/mcbot delGroup` 参数名不一致和 `/mcbot reload` 空操作。
- 移除 FTB 等模组产生的 Minecraft/ANSI 颜色控制码，保留换行并安全分段复杂命令文本。
- 群命令写入服务端控制台审计日志，事件注册改为幂等，历史配置中的重复群号不再造成重复转发。
- 1.7.10 修复误用 `Long.getLong` 导致的 `setBot` 空值，并通过 Java 8 完整构建。

### 验证与限制

- 1.20.1 Fabric/Forge 与 1.21.1 Fabric/NeoForge 已通过单元测试和完整构建。
- 旧版 Fabric/Forge 1.16.5、1.18.2、1.19.2 已通过本地完整构建。
- 1.12.2 仍被已下架的 `OneBot-SDK:0.1.4` 制品阻塞；图片化复杂命令输出与特定模组组合仍需后续实现或实机回归。

## [3.0.0](https://github.com/Minecraft0122/McBot-Reloaded/releases/tag/3.0.0) - 2026-07-28

### 版本与维护

- 模组版本从 3.0.0 重新编号，新标签不再使用 `v`、`release` 等前缀。
- 正式维护 Minecraft 1.20.1 Fabric/Forge 与 1.21.1 Fabric/NeoForge，四个组合均通过单元测试和完整构建。
- 当前维护者和作者列表增加 Minecraft0122，仓库联系信息、问题入口、CODEOWNERS 和发布地址统一到 Minecraft0122/McBot-Reloaded。
- 保留 GPL-3.0-or-later、原作者 cnlimiter、Nova Committee、历史贡献者署名和 Git 历史。

### 修复

- 吸收上游 PR #197，支持 `wss://`、`ws://` 和无协议 OneBot 地址，并拒绝 HTTP 等错误协议。
- 修复 Minecraft 1.21.1 中无展示信息的进度触发 `Optional` 空值并崩服（上游 #187、#190、#196）。
- 命令响应使用独立捕获对象并有界等待异步输出，避免 Spark/TabTPS 结果为空或串到下一条命令（上游 #79、#115）。
- 服务器命令结果以纯文本发送并按行安全分段，避免复杂健康报告被误解析为 CQ 码（上游 #159）。
- 将 2.3.1 的群名片、`@`、换行、删群、空回复、绑定、权限、数据和停服修复移植到 1.21.1。

### 构建与发布

- 产物统一命名为 `McBot-<Minecraft>-3.0.0-<加载器>.jar`。
- `3.0.0` 标签自动从两个正式版本分支构建四个可安装 JAR，生成 SHA-256 校验和与构建来源证明并创建 GitHub Release。
- 新增现有版本、全部上游 issue 和全部上游 PR 的中文维护矩阵。

## [2.3.1](https://github.com/Minecraft0122/McBot-Reloaded/releases/tag/v2.3.1) - 2026-07-27

### 修复

- 修复群名片开关逻辑写反，并在群名片为空时回退到 QQ 昵称或账号（上游 [#182](https://github.com/Nova-Committee/McBot/issues/182)）。
- 修复 QQ `@` 消息缺少名称时在游戏中显示 `@[null]`，现在会回退到 QQ 号（上游 [#183](https://github.com/Nova-Committee/McBot/issues/183)）。
- 保留自定义命令多段输出之间的换行（上游 [#184](https://github.com/Nova-Committee/McBot/issues/184)）。
- 修复 `/mcbot delGroup` 读取了错误参数名的问题（上游 [#185](https://github.com/Nova-Committee/McBot/issues/185)）。
- 忽略无内容的命令回复，避免 OneBot 拒绝空消息（上游 [#186](https://github.com/Nova-Committee/McBot/issues/186)）。
- 修复自定义命令参数解析时修改正在遍历的列表、别名误匹配和单词前缀越界问题。
- 修复 `ALL` 权限未生效、`say` 多词参数被截断和默认取消绑定命令拼写错误。
- 停服时同步保存数据、先关闭 OneBot 连接再关闭线程池，避免重连线程阻止进程退出。
- 进度没有显示信息时不再转发，并移除对格式化结果的二次格式化。
- 修复绑定校验开启后所有玩家都会被踢出，以及同群多名用户查询提前返回的问题。
- 修复聊天记录错误地用机器人账号作为键和消息发送者的问题。
- 修复 `/mcbot send all false` 仍会开启全局发送的问题。
- 串行化 QQ 侧命令执行，避免并发命令的多行响应互相混入。
- 连接失败时不再将 WebSocket 标记为已连接，断开后清除失效客户端，并补齐异步发送异常日志。
- 旧配置缺少或包含无效版本号时先备份再重建，避免配置加载空指针。
- 仅将 OneBot 的 `admin` 和 `owner` 角色识别为群管理员，未知角色不再获得管理员权限。

### 本地化与清理

- 重写简体中文 README、帮助文本、状态文本、模组描述和 GitHub 提交模板。
- 简体中文改为语言文件缺失时的默认回退语言。
- 删除本地机器人启动器移除后遗留的下载、解压、配置和空占位代码。
- 修复 GitHub 发布工作流中的模块名、无效发布任务和重复标签问题。
- 将失效的 Nova Maven 依赖源替换为 OneBot Client 官方 GitHub Release，并固定动态依赖版本。
- 将 Minecraft 兼容声明收紧为实际构建目标 1.20.1，并补齐 Fabric Loader、Fabric API、Java 和 Forge 最低版本约束。
- 合并重复 CI，在每次分支推送和拉取请求中执行测试及 Fabric/Forge 完整构建。
- 添加 `v<mod_version>` 标签触发的自动发布、正式产物筛选、SHA-256 校验和及 GitHub 构建来源证明。
- 将 Architectury Loom 与 Architectury Plugin 从可变的快照版本改为固定发行版本，使 CI 和发布构建可重复。
- 使用 Forge 47.3 提供的加载上下文注入注册客户端配置界面，移除已标记待删除的静态上下文调用。

## [v2.3.0-release](https://github.com/Nova-Committee/McBot/compare/v2.3.0-release...master)

### 新增

- 添加本地机器人执行支持（[e1f51d4](https://github.com/Nova-Committee/McBot/commit/e1f51d4)）。
- 使用 Jupiter Config 管理配置（[28a1a82](https://github.com/Nova-Committee/McBot/commit/28a1a82)）。
- 添加模组配置界面（[8a823d2](https://github.com/Nova-Committee/McBot/commit/8a823d2)）。
- 优化命令解析并添加日志（[185552a](https://github.com/Nova-Committee/McBot/commit/185552a)）。
- 添加玩家绑定与聊天记录 API（[9af0567](https://github.com/Nova-Committee/McBot/commit/9af0567)）。
- 添加权限支持与服务器标识（[230bd22](https://github.com/Nova-Committee/McBot/commit/230bd22)）。
- 为 Fabric 和 Forge 添加刻末事件（[c934fb1](https://github.com/Nova-Committee/McBot/commit/c934fb1)）。
- 支持自动注册配置项（[c914724](https://github.com/Nova-Committee/McBot/commit/c914724)）。
- 集成 OneBot API，实现 QQ 群与 Minecraft 的消息、玩家事件和生命周期事件转发（[3ef9449](https://github.com/Nova-Committee/McBot/commit/3ef9449)）。
- 添加命令与事件处理（[c8e24e0](https://github.com/Nova-Committee/McBot/commit/c8e24e0)）。
- 添加面向模组兼容的动态事件系统（[ec5d9f3](https://github.com/Nova-Committee/McBot/commit/ec5d9f3)）。
- 添加 Minecraft 1.21 Fabric 构建入口（[2907d86](https://github.com/Nova-Committee/McBot/commit/2907d86)）。

### 修复

- 阻止登录时绑定无效游戏名（[8d1b0c4](https://github.com/Nova-Committee/McBot/commit/8d1b0c4)）。
- 更新依赖并修复 CQUtils 转义字符（[a5c6a23](https://github.com/Nova-Committee/McBot/commit/a5c6a23)）。
- 修复运行时 JAR 依赖未正确关联的问题（[e6d02b5](https://github.com/Nova-Committee/McBot/commit/e6d02b5)、[c9e71d9](https://github.com/Nova-Committee/McBot/commit/c9e71d9)）。

### 文档、性能与重构

- 更新 README 中的 JetBrains 标志和说明（[c726117](https://github.com/Nova-Committee/McBot/commit/c726117)）。
- 优化线程池和关闭顺序（[48234ce](https://github.com/Nova-Committee/McBot/commit/48234ce)、[b2349b3](https://github.com/Nova-Committee/McBot/commit/b2349b3)）。
- 重构配置项、命令解析、日志、事件和工具类（[419e6a9](https://github.com/Nova-Committee/McBot/commit/419e6a9)、[da3e88b](https://github.com/Nova-Committee/McBot/commit/da3e88b)、[2bbd36e](https://github.com/Nova-Committee/McBot/commit/2bbd36e)）。
- 将机器人账号配置改为字符串，避免数值范围问题（[f5fb026](https://github.com/Nova-Committee/McBot/commit/f5fb026)）。
- 清理未使用代码（[120f892](https://github.com/Nova-Committee/McBot/commit/120f892)、[06e30b1](https://github.com/Nova-Committee/McBot/commit/06e30b1)）。
- 重做权限占位符、用户信息与命令处理（[46ce02c](https://github.com/Nova-Committee/McBot/commit/46ce02c)、[4e0fd28](https://github.com/Nova-Committee/McBot/commit/4e0fd28)、[89d36a9](https://github.com/Nova-Committee/McBot/commit/89d36a9)）。
- 优化玩家绑定、国际化和序列化（[e6d8ebe](https://github.com/Nova-Committee/McBot/commit/e6d8ebe)、[3185b0b](https://github.com/Nova-Committee/McBot/commit/3185b0b)、[2dcf5fa](https://github.com/Nova-Committee/McBot/commit/2dcf5fa)）。
- 调整配置保存、构建脚本、工作流和配置格式（[4766fec](https://github.com/Nova-Committee/McBot/commit/4766fec)、[79b512e](https://github.com/Nova-Committee/McBot/commit/79b512e)、[83fe0c2](https://github.com/Nova-Committee/McBot/commit/83fe0c2)、[9887c8e](https://github.com/Nova-Committee/McBot/commit/9887c8e)、[98cb5bb](https://github.com/Nova-Committee/McBot/commit/98cb5bb)、[b6d60ea](https://github.com/Nova-Committee/McBot/commit/b6d60ea)）。
- 调整发布标签、构建平台和 Gradle Wrapper 调用方式（[d3a2fc1](https://github.com/Nova-Committee/McBot/commit/d3a2fc1)、[587dd56](https://github.com/Nova-Committee/McBot/commit/587dd56)、[10736a9](https://github.com/Nova-Committee/McBot/commit/10736a9)）。
- 统一工作流中的加载器名称（[475617f](https://github.com/Nova-Committee/McBot/commit/475617f)）。

## [v2.2.1.1-fix3-fabric](https://github.com/Nova-Committee/McBot/compare/v2.2.1.1-fix3-fabric...master)

- 优化代码格式（[62729a4](https://github.com/Nova-Committee/McBot/commit/62729a4)、[fe5f42e](https://github.com/Nova-Committee/McBot/commit/fe5f42e)）。

## [v2.2.1.1-fix2-fabric](https://github.com/Nova-Committee/McBot/compare/v2.2.1.1-fix2-fabric...master)

- 修复进程无法正常关闭（[6c9b941](https://github.com/Nova-Committee/McBot/commit/6c9b941)）。

## [v2.2.1.1-fix1-fabric](https://github.com/Nova-Committee/McBot/compare/v2.2.1.1-fix1-fabric...master)

- 取消运行时下载机制（[d9165af](https://github.com/Nova-Committee/McBot/commit/d9165af)）。

## [v2.2.1.1-fabric](https://github.com/Nova-Committee/McBot/compare/v2.2.1.1-fabric...master)

### 新增与修复

- 添加消息撤回记录 API 和群服绑定 API（[3026614](https://github.com/Nova-Committee/McBot/commit/3026614)、[3d0e64a](https://github.com/Nova-Committee/McBot/commit/3d0e64a)、[16ccf52](https://github.com/Nova-Committee/McBot/commit/16ccf52)、[f16937b](https://github.com/Nova-Committee/McBot/commit/f16937b)）。
- 修复监听器重复注册、预处理、命令结果重复、进程关闭、消息链和语言文件问题（[4788fa8](https://github.com/Nova-Committee/McBot/commit/4788fa8)、[7b29191](https://github.com/Nova-Committee/McBot/commit/7b29191)、[8a8c859](https://github.com/Nova-Committee/McBot/commit/8a8c859)、[c048a10](https://github.com/Nova-Committee/McBot/commit/c048a10)、[c96380e](https://github.com/Nova-Committee/McBot/commit/c96380e)、[cbd7432](https://github.com/Nova-Committee/McBot/commit/cbd7432)）。
- 支持 OpenShamrock 消息链（[89eeabe](https://github.com/Nova-Committee/McBot/commit/89eeabe)）。
- 适配 OneBot Client 导入变化并优化 Mixin（[0b06e3d](https://github.com/Nova-Committee/McBot/commit/0b06e3d)、[eef9b51](https://github.com/Nova-Committee/McBot/commit/eef9b51)）。
- 更新 OneBot Client 并适配各版本 Vanish（[952078a](https://github.com/Nova-Committee/McBot/commit/952078a)、[9ba5a1f](https://github.com/Nova-Committee/McBot/commit/9ba5a1f)）。
- 修复构建与重映射配置（[08adc4f](https://github.com/Nova-Committee/McBot/commit/08adc4f)、[3a51a81](https://github.com/Nova-Committee/McBot/commit/3a51a81)）。

## [v2.1.9.1-fabric](https://github.com/Nova-Committee/McBot/compare/v2.1.9.1-fabric...master)

- 修复国际化与多个历史 issue（[#120](https://github.com/Nova-Committee/McBot/issues/120)、[#109](https://github.com/Nova-Committee/McBot/issues/109)、[#96](https://github.com/Nova-Committee/McBot/issues/96)、[#97](https://github.com/Nova-Committee/McBot/issues/97)、[#93](https://github.com/Nova-Committee/McBot/issues/93)、[#92](https://github.com/Nova-Committee/McBot/issues/92)、[#89](https://github.com/Nova-Committee/McBot/issues/89)、[#81](https://github.com/Nova-Committee/McBot/issues/81)、[#80](https://github.com/Nova-Committee/McBot/issues/80)、[#76](https://github.com/Nova-Committee/McBot/issues/76)、[#75](https://github.com/Nova-Committee/McBot/issues/75)、[#74](https://github.com/Nova-Committee/McBot/issues/74)、[#73](https://github.com/Nova-Committee/McBot/issues/73)、[#62](https://github.com/Nova-Committee/McBot/issues/62)、[#36](https://github.com/Nova-Committee/McBot/issues/36)、[#32](https://github.com/Nova-Committee/McBot/issues/32)、[#22](https://github.com/Nova-Committee/McBot/issues/22)、[#20](https://github.com/Nova-Committee/McBot/issues/20)）。
- 修复命令执行与发布工作流（[ddde41b](https://github.com/Nova-Committee/McBot/commit/ddde41b)、[247d8d4](https://github.com/Nova-Committee/McBot/commit/247d8d4)、[ef869cb](https://github.com/Nova-Committee/McBot/commit/ef869cb)、[2590d13](https://github.com/Nova-Committee/McBot/commit/2590d13)、[38e070d](https://github.com/Nova-Committee/McBot/commit/38e070d)）。
