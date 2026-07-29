# GitHub 测试规范

McBot Reloaded 将 GitHub Actions 作为合并、兼容性和发布判定的唯一正式测试来源。本地测试只用于开发反馈，不能代替 GitHub 的质量门禁。

完整、可版本控制的规范见仓库根目录的 [`TESTING_STANDARD.md`](https://github.com/Minecraft0122/McBot-Reloaded/blob/1.20.1/TESTING_STANDARD.md)。

## 当前必测矩阵

| Minecraft | 加载器 | 构建 JDK | 真实服务端运行 JDK |
| --- | --- | --- | --- |
| 1.20.1 | Fabric 0.19.3+ | 17 | 17、21、25 |
| 1.20.1 | Forge 47.4.22–47.x | 17 | 17、21、25 |
| 1.21.1 | Fabric 0.19.3+ | 21 | 21、25 |
| 1.21.1 | NeoForge 21.1.244–21.1.x | 21 | 21、25 |

## GitHub 执行方式

- 每次推送或拉取请求：测试当前版本分支的全部加载器和 Java 运行矩阵。
- 手动测试：在 Actions 页面选择“持续集成”或“全版本巡检”运行。
- 每周一 02:00（北京时间）：自动执行全部维护版本巡检。
- 全版本巡检失败：自动创建或更新 GitHub Issue，附带失败矩阵和工作流链接。
- 创建版本标签：发布工作流重新执行全部构建和真实服务端测试；任一失败都不会创建 Release。

每个正式分支使用一个名称固定为 `质量门禁` 的汇总检查。只有编译、单元测试、正式 JAR 构建以及全部真实服务端启动/关服测试成功时，该检查才会通过。

## 服务端通过条件

每个组合必须使用本次构建的正式 JAR，在干净目录安装生产服务端，使用矩阵指定的 Java 启动到 `Done`，确认 McBot 已加载，然后通过 `stop` 正常关闭。日志不得出现类版本、模块访问、链接、初始化、配置加载或崩溃错误。

GitHub 会保留正式 JAR、测试报告、服务端日志和崩溃报告；正式发布还会生成 SHA-256 清单和构建来源证明。

## 查看结果

- [全部 GitHub Actions](https://github.com/Minecraft0122/McBot-Reloaded/actions)
- [持续集成工作流](https://github.com/Minecraft0122/McBot-Reloaded/actions/workflows/build.yml)
- [全版本巡检工作流](https://github.com/Minecraft0122/McBot-Reloaded/actions/workflows/repository-test.yml)
- [自动发布工作流](https://github.com/Minecraft0122/McBot-Reloaded/actions/workflows/publish.yml)
