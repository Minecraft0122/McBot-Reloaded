# 第三方组件说明

旧版 Fabric 聚合构建会把 [cnlimiter/onebot-client](https://github.com/cnlimiter/onebot-client) 0.4.1（AGPL-3.0）打入最终 JAR，并通过 Maven Central 在首次启动时取得 Configurate HOCON 4.1.2 及其依赖。AGPL-3.0 文本见 `LICENSES/AGPL-3.0.txt`。

OneBot Client 使用其官方 GitHub Release 固定版本下载，不再依赖已失效的 Nova Committee Maven 仓库。
