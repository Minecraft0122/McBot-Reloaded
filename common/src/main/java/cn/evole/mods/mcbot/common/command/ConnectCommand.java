package cn.evole.mods.mcbot.common.command;

import cn.evole.mods.mcbot.Constants;
import cn.evole.mods.mcbot.api.connect.ConnectApi;
import cn.evole.mods.mcbot.common.config.BotConfig;
import cn.evole.mods.mcbot.common.config.ModConfig;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class ConnectCommand {
    public static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String parameter = context.getArgument("parameter", String.class);

        try {
            String normalizedUrl = BotConfig.normalizeWebSocketUrl(parameter);
            ModConfig.get().getBotConfig().getUrl().setValueFromString(normalizedUrl);
            ModConfig.get().save();
            doConnect(context);
            return 1;
        } catch (IllegalArgumentException e) {
            context.getSource().sendFailure(Component.literal("▌ " + ChatFormatting.RED + "参数错误：" + e.getMessage()));
            return 0;
        }
    }

    public static int commonExecute(CommandContext<CommandSourceStack> context) {
        doConnect(context);
        return 1;
    }

    public static void doConnect(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (ConnectApi.isConnected()) {
            source.sendSuccess(() -> Component.literal("▌ " + ChatFormatting.LIGHT_PURPLE + "WebSocket 已连接"), true);
            return;
        }

        source.sendSuccess(() -> Component.literal("▌ " + ChatFormatting.LIGHT_PURPLE + "正在尝试连接机器人框架"), true);
        ConnectApi.wsConnectAsync().whenComplete((connected, error) -> {
            if (error != null) {
                Constants.LOGGER.error("异步连接 OneBot WebSocket 失败", error);
            }

            source.getServer().execute(() -> {
                if (error == null && Boolean.TRUE.equals(connected)) {
                    source.sendSuccess(() -> Component.literal("▌ " + ChatFormatting.GREEN + "WebSocket 连接成功"), true);
                } else {
                    source.sendFailure(Component.literal("▌ " + ChatFormatting.RED + "连接失败，请检查地址、令牌和服务端日志"));
                }
            });
        });
    }
}
