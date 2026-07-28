package cn.evole.mods.mcbot.common.command;

import cn.evole.mods.mcbot.Constants;
import cn.evole.mods.mcbot.common.config.ModConfig;
import cn.evole.mods.mcbot.plugins.cmd.CmdHandler;
import cn.evole.mods.mcbot.util.locale.I18n;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/28 13:37
 * Version: 1.0
 */
public class ReloadConfigCmd {
    public static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        try {
            ModConfig.get().load();
            I18n.reload();
            CmdHandler.load();
            context.getSource().sendSuccess(() -> Component.literal("配置与自定义命令已重新加载。"), true);
        } catch (Exception e) {
            Constants.LOGGER.error("重新加载配置失败", e);
            context.getSource().sendFailure(Component.literal("重新加载配置失败，请查看服务器日志。"));
            return 0;
        }
        
        return 1;
    }
}
