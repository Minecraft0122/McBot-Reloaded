# 第三方组件说明

旧版 Forge 聚合构建包含以下不可缺少的第三方组件：

| 组件 | 来源 | 固定版本 | 许可证 |
| --- | --- | --- | --- |
| AtomConfig-Toml | [cnlimiter/AtomConfig](https://github.com/cnlimiter/AtomConfig) | `78568e625ad82ca2106544400c15d78011feb398` / 0.1.5 | AGPL-3.0 |
| OneBot Client | [cnlimiter/onebot-client](https://github.com/cnlimiter/onebot-client) | 0.4.3 | AGPL-3.0 |

`forge/libs/AtomConfig-Toml-0.1.5.jar` 由上述固定提交使用 Java 8 执行 `gradlew clean shadowJar` 生成，SHA-256 为 `2ae5ba28ccc20f3762469523dc776ba49a129328899f03c2bcabdceb9f2168b4`。保留该制品是因为原 Nova Committee Maven 地址已失效；固定源码提交和校验和避免使用来源不明的同名 JAR。AGPL-3.0 文本见 `LICENSES/AGPL-3.0.txt`。
