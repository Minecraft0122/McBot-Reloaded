# 发布流程

本页面向维护者。普通用户应直接从 [GitHub Releases](https://github.com/Minecraft0122/McBot-Reloaded/releases/latest)下载。

## 版本规则

- 当前版本线从 `3.0.0` 重新编号。
- 正式标签只包含版本号，例如 `3.0.1`，不使用 `v3.0.1`、`release-3.0.1` 等前缀。
- 标签必须与正式分支 `gradle.properties` 中的 `mod_version` 完全一致。
- JAR 文件保留 Minecraft 版本和加载器后缀，防止用户混装。

## 发布前检查

1. `1.20.1` 和 `1.21.1` 的 `mod_version` 相同。
2. 两个正式分支工作区干净并已推送。
3. 更新 `CHANGELOG.md`、README、Wiki 和支持版本表。
4. 使用目标 JDK 运行完整测试和干净构建。
5. 检查 JAR 文件名、模组元数据、作者和维护者信息。
6. 确认没有 Token、签名密钥、私有地址或测试账号进入仓库和构建产物。

## 自动发布

推送纯版本号标签：

```bash
git tag -a 3.0.1 -m "McBot 3.0.1"
git push origin 3.0.1
```

`.github/workflows/publish.yml` 会：

1. 从 `1.20.1` 分支使用 Java 17 构建 Fabric 和 Forge。
2. 从 `1.21.1` 分支使用 Java 21 构建 Fabric 和 NeoForge。
3. 运行各分支测试和完整构建。
4. 校验标签、`mod_version` 和 Minecraft 版本。
5. 只收集四个可安装的加载器 JAR。
6. 为 JAR 生成 GitHub 构建来源证明。
7. 汇总产物并生成 `SHA256SUMS.txt`。
8. 创建非草稿、非预发布的 GitHub Release。

任一构建失败时不会创建不完整 Release。

## 正式产物

一个正式版本应包含：

```text
McBot-1.20.1-<版本>-fabric.jar
McBot-1.20.1-<版本>-forge.jar
McBot-1.21.1-<版本>-fabric.jar
McBot-1.21.1-<版本>-neoforge.jar
SHA256SUMS.txt
```

开发包、源码包、未重映射 JAR 和测试报告不能作为面向普通用户的 Release 附件。

## 发布后验证

- Release 不是 Draft 或 Prerelease。
- 四个 JAR 均可下载，文件名和大小合理。
- `SHA256SUMS.txt` 与实际资产完全一致。
- GitHub Actions 的发布工作流为成功状态。
- `releases/latest` 指向新版本。
- Wiki 首页和安装页没有把旧版本标成最新正式版。

## 历史版本

历史维护分支可以通过 CI 上传短期构建产物，但在真实服务端关键回归完成前，不应混入正式 Release。1.12.2 在缺失可信上游依赖的情况下不得发布拼装或来源不明的 JAR。

