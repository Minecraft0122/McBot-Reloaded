# 安装教程

## 1. 选择正确版本

先查看[兼容版本](Compatibility)。正式 Release 当前提供以下组合：

| Minecraft | 加载器 | 已验证 Java | 文件名结尾 |
| --- | --- | --- | --- |
| 1.20.1 | Fabric Loader 0.19.3+ | 17、21、25 | `-fabric.jar` |
| 1.20.1 | Forge 47.4.22—47.x | 17、21、25 | `-forge.jar` |
| 1.21.1 | Fabric Loader 0.19.3+ | 21、25 | `-fabric.jar` |
| 1.21.1 | NeoForge 21.1.244+（21.1.x） | 21、25 | `-neoforge.jar` |

例如，Minecraft 1.20.1 Forge 服务端必须使用 `McBot-1.20.1-版本号-forge.jar`。不能把 Fabric JAR 安装到 Forge，也不能把 1.21.1 JAR 安装到 1.20.1。

Java 21、25 均由 GitHub Actions 进行真实服务端启动和关服测试。若使用 1.20.1，Java 17 仍是最低版本；若使用 1.21.1，Java 21 是最低版本。Java 18—20、22—24 没有纳入持续兼容矩阵。

## 2. 下载并校验

1. 打开[最新正式版](https://github.com/Minecraft0122/McBot-Reloaded/releases/latest)。
2. 下载与服务端匹配的 JAR。
3. 可选但推荐：同时下载 `SHA256SUMS.txt`，使用 SHA-256 核对完整性。

Windows PowerShell 校验示例：

```powershell
Get-FileHash .\McBot-1.20.1-3.0.4-forge.jar -Algorithm SHA256
```

Linux 校验示例：

```bash
sha256sum McBot-1.20.1-3.0.4-forge.jar
```

不要使用 Release 页面底部由 GitHub 自动生成的 `Source code (zip)` 或 `Source code (tar.gz)`；它们是源码，不是可安装模组。

## 3. 安装模组

### Fabric

- 安装与 Minecraft 版本匹配的 Fabric Loader。
- 在 `mods` 目录中只放入对应版本的 McBot JAR；不需要 Fabric API、Jupiter 或其他前置模组。
- Mod Menu 是可选依赖，只在需要客户端配置界面时安装。

### Forge / NeoForge

- 安装与 Minecraft 版本匹配的 Forge 或 NeoForge。
- 把对应 McBot JAR 放入 `mods` 目录。
- 不要额外安装 Fabric API。

配置核心和 OneBot Client 已打入正式 JAR，普通用户不需要单独下载。

## 4. 首次启动

启动一次服务端。McBot 将在服务端工作目录生成：

```text
mcbot/
├─ config.json
├─ cmds/
│  ├─ list.json
│  ├─ say.json
│  ├─ bind.json
│  └─ unbind.json
└─ data/
   ├─ chatRecord.csv
   └─ userBind.csv
```

CSV 文件可能要在产生相应数据后才包含内容。首次生成完成后，正常停止服务端再编辑配置。

## 5. 服务端与客户端

McBot 是服务端模组。专用服务器上的普通玩家客户端无需安装；只要服务器正常加载，玩家即可通过原版客户端加入。

如果在单人游戏或集成服务器中使用，可以在客户端安装 McBot。Fabric 环境配合 Mod Menu 时可打开配置界面，但 OneBot 连接仍由启动该世界的 Minecraft 实例负责。

## 6. 升级

1. 停止 Minecraft 服务端。
2. 备份 `mcbot/` 目录、世界存档和旧 JAR。
3. 删除旧 McBot JAR，只放入一个新 JAR。
4. 启动并检查日志，然后执行 `/mcbot status`。

配置包含版本字段。若配置结构版本不匹配，McBot 会在 `mcbot/` 中备份旧配置并生成新配置；请手工迁移必要值，不要直接覆盖新结构。

## 7. 卸载

停止服务端并移除 McBot JAR 即可。若不再需要历史配置、绑定和聊天记录，可在确认备份后手工处理 `mcbot/` 目录。普通卸载不会要求玩家修改客户端。
