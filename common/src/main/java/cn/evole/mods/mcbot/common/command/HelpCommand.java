package cn.evole.mods.mcbot.common.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class HelpCommand {
    private static final String ISSUE_URL = "https://github.com/Minecraft0122/McBot-Reloaded/issues/new";

    public static int execute(CommandContext<CommandSourceStack> context) {
        String help = """
                \nMcBot 群服互联使用说明：
                1. 在 NapCatQQ、Lagrange.OneBot 等框架中启用正向 WebSocket。
                2. 使用 /mcbot addGroup <群号> 添加互通群。
                3. 使用 /mcbot setBot <QQ号> 设置机器人账号。
                4. 如启用了访问令牌，使用 /mcbot setAuthKey <令牌> 设置。
                5. 使用 /mcbot connect [主机:端口] 建立连接。
                *************************************
                /mcbot disconnect                    断开连接
                /mcbot delGroup <群号>               删除互通群
                /mcbot receive <all|chat|cmd> <开关> 控制接收内容
                /mcbot send <类型> <开关>            控制事件转发
                /mcbot status                        查看服务状态
                /mcbot customs                       列出自定义命令
                /mcbot reload                        重新加载配置
                *************************************
                遇到问题请提交完整日志和已脱敏的配置：
                """;

        Component link = Component.literal(ISSUE_URL)
                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, ISSUE_URL)));
        context.getSource().sendSuccess(() -> Component.literal(help).append(link), true);
        return 1;
    }
}
