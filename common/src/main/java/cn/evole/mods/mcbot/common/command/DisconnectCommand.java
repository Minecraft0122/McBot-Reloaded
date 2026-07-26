package cn.evole.mods.mcbot.common.command;


import cn.evole.mods.mcbot.api.connect.ConnectApi;
import cn.evole.mods.mcbot.common.config.ModConfig;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class DisconnectCommand {

    public static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (ConnectApi.wsDisconnect()) {
            context.getSource().sendSuccess(() -> Component.literal("WebSocket 已断开连接"), true);
            ModConfig.get().getCommon().getEnable().setValue(false);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("WebSocket 当前未连接"), true);
        }
        return 1;
    }
}
