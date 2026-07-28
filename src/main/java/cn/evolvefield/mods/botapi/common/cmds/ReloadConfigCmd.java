package cn.evolvefield.mods.botapi.common.cmds;

import cn.evolvefield.mods.botapi.BotApi;
import cn.evolvefield.mods.botapi.init.handler.ConfigHandler;
import cn.evolvefield.mods.botapi.init.handler.CustomCmdHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/28 13:37
 * Version: 1.0
 */
public class ReloadConfigCmd {
    public static void execute(ICommandSender sender, String[] args) throws CommandException {
        try {
            ConfigHandler.load(BotApi.CONFIG_FILE);
            if (ConfigHandler.cached() == null) {
                sender.addChatMessage(new ChatComponentText("重载配置失败"));
            }
            CustomCmdHandler.INSTANCE.load(BotApi.CONFIG_FOLDER);
            sender.addChatMessage(new ChatComponentText("重载配置成功"));
        } catch (Exception e) {
            sender.addChatMessage(new ChatComponentText("重载配置失败"));
        }
        ConfigHandler.save();

    }
}
