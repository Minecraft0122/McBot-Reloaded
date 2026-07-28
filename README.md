# McBot Reloaded（Minecraft 1.12.2 Forge）

这是 McBot 的 Minecraft 1.12.2 Forge 兼容维护分支。当前维护者为 **Minecraft0122**；代码源自 cnlimiter 与 Nova Committee，原作者、许可证和 Git 历史依照 GPL-3.0-or-later 保留。

## 当前状态

| 项目 | 范围 |
| --- | --- |
| Minecraft | 1.12.2 |
| Forge | 14.23.5.2860 |
| Java | 8 |
| McBot 源码版本 | 3.0.0 |

本分支暂不提供可供普通用户安装的 3.0.0 JAR。原工程依赖的 `cn.evolvefield.bot:OneBot-SDK:0.1.4` 已从 Nova Committee Maven 仓库移除，公开地址返回 404；在找到可验证的原始制品或完成 Java 8 OneBot 客户端替换前，无法诚实声明构建和运行兼容。

不要使用来源不明的同名 JAR 绕过此限制。维护工作会保留在本分支，正式可用版本请使用仓库 Release 中列出的 1.20.1 或 1.21.1 产物。

## 构建诊断

```bash
./gradlew clean build --no-daemon --stacktrace
```

若能提供原始 OneBot SDK 0.1.4 制品、校验和或 1.12.2 完整启动日志，请在 [Minecraft0122/McBot-Reloaded Issues](https://github.com/Minecraft0122/McBot-Reloaded/issues) 提交。项目依据 [GNU GPL 3.0 或更高版本](LICENSE) 发布。
