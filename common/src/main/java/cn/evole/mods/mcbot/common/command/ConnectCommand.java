package cn.evole.mods.mcbot.common.command;


import cn.evole.mods.mcbot.Constants;
import cn.evole.mods.mcbot.api.connect.ConnectApi;
import cn.evole.mods.mcbot.common.config.BotConfig;
import cn.evole.mods.mcbot.common.config.ModConfig;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.val;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.regex.Pattern;

public class ConnectCommand {
    private static final Pattern IPV4_PATTERN = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)");
    private static final Pattern IPV6_PATTERN = Pattern.compile("\\[([0-9a-fA-F:]+)]:(\\d+)");
    private static final Pattern DOMAIN_PATTERN = Pattern.compile("([a-zA-Z0-9.-]+):(\\d+)");

    public static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        val parameter = context.getArgument("parameter", String.class);

        if (IPV4_PATTERN.matcher(parameter).matches() || IPV6_PATTERN.matcher(parameter).matches() || DOMAIN_PATTERN.matcher(parameter).matches()) {
            ModConfig.get().getBotConfig().getUrl().setValueFromString(BotConfig.normalizeWebSocketUrl(parameter));
            doConnect(context);
            return 1;
        } else {
            context.getSource().sendSuccess(() -> Component.literal("▌ " + ChatFormatting.RED + "参数错误❌"), true);
            return 0;
        }
    }


    public static int commonExecute(CommandContext<CommandSourceStack> context) {
        doConnect(context);
        return 1;
    }

    public static void doConnect(CommandContext<CommandSourceStack> context) {
        if (!ConnectApi.isConnected()) {
            context.getSource().sendSuccess(() -> Component.literal("▌ " + ChatFormatting.LIGHT_PURPLE + "正在尝试连接机器人框架"), true);
            if (!ConnectApi.wsConnect()) {
                context.getSource().sendFailure(Component.literal("▌ " + ChatFormatting.RED + "连接失败，请检查地址、令牌和服务端日志"));
            }
        } else {
            context.getSource().sendSuccess(() -> Component.literal("▌ " + ChatFormatting.LIGHT_PURPLE + "WebSocket 已连接"), true);
        }
    }
}
