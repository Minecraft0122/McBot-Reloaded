# 自定义群命令

自定义命令位于 `mcbot/cmds/`。首次启动会生成 `list.json`、`say.json`、`bind.json` 和 `unbind.json`。每个 JSON 文件定义一个命令；修改后执行 `/mcbot reload` 即可热重载。

## 最小示例

```json
{
  "id": "list",
  "cmd": "list",
  "alies": ["服务器在线"],
  "allow_members": [],
  "permission": "ALL",
  "after_cmds": [],
  "answer": "NO",
  "enable": true
}
```

QQ群成员可以发送 `!list` 或 `!服务器在线`。`!` 来自主配置中的 `cmd.cmdStart`。

## 字段说明

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | 字符串 | 唯一命令 ID，也是权限名称的一部分 |
| `cmd` | 字符串 | 在 Minecraft 服务端执行的命令，不要带 `/` |
| `alies` | 字符串数组 | 命令别名；字段名历史上拼写为 `alies`，不要改成 `aliases` |
| `allow_members` | 字符串数组 | 无条件允许执行该命令的 QQ 号 |
| `permission` | 字符串 | `ALL` 表示所有人；其他值进入绑定权限检查 |
| `after_cmds` | 字符串数组 | 当前命令完成后继续执行的自定义命令 ID 或服务端命令 |
| `answer` | 字符串 | 服务端命令没有输出时的备用回复；`NO` 表示不使用备用回复 |
| `enable` | 布尔值 | `false` 时不加载该文件 |

## 变量

| 变量 | 替换内容 |
| --- | --- |
| `%group_id%` | 来源 QQ 群号 |
| `%user_id%` | 发送者 QQ 号 |
| `%user_age%` | OneBot 上报的年龄 |
| `%user_nickname%` | OneBot 上报的昵称 |
| `%` | 按顺序替换为群命令参数；最后一个 `%` 会接收剩余全部参数 |

示例：

```json
{
  "id": "say",
  "cmd": "say %",
  "alies": ["转发"],
  "allow_members": ["123456789"],
  "permission": "OP",
  "after_cmds": [],
  "answer": "转发成功！",
  "enable": true
}
```

群内发送：

```text
!say 服务器将在十分钟后维护
```

服务端执行：

```text
say 服务器将在十分钟后维护
```

## 权限规则

按以下顺序判定：

1. QQ 群主和群管理员可执行已匹配的命令。
2. `permission` 为 `ALL` 时，所有群成员可执行。
3. QQ 号出现在 `allow_members` 时可执行。
4. 已绑定用户可通过 `userBind.csv` 中的权限执行；权限名称形如 `<tag>.mcbot.cmd.<id>`。

默认 tag 是 `main`，所以 `list` 命令权限通常是 `main.mcbot.cmd.list`。默认绑定用户拥有 `mcbot.cmd.tps` 和 `mcbot.cmd.list`，保存时会加上当前 tag。

`permission` 的 `OP` 不是 Minecraft OP 的自动映射；它表示该命令不向所有人开放，实际访问仍由群管理员、`allow_members` 或绑定权限决定。

## 连续命令

`after_cmds` 可以引用另一个自定义命令 ID：

```json
{
  "id": "maintenance",
  "cmd": "say 服务器即将维护",
  "alies": ["维护"],
  "allow_members": [],
  "permission": "OP",
  "after_cmds": ["save-all"],
  "answer": "维护通知已发送",
  "enable": true
}
```

请谨慎使用连续命令，避免循环引用。不要把 `stop`、权限授予、文件操作或任意命令执行能力开放给 `ALL`。

## 安全建议

- 对 JSON 做语法校验，文件编码使用 UTF-8。
- 命令 ID 保持唯一，文件名建议与 ID 一致。
- 高权限命令只允许群主/管理员或明确 QQ 号。
- 不要把用户可控参数直接拼入权限、白名单或管理命令。
- 修改后先执行 `/mcbot reload`，再用 `/mcbot customs` 确认已加载。
- 服务端控制台会记录来源群号、用户、命令 ID 和返回字符数，但不会主动记录 OneBot Token。

