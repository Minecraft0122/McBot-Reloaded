package cn.evolvefield.mods.botapi.common.cmds;

import cn.evolvefield.mods.botapi.init.handler.ConfigHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class BotIDCommand {


    public static void execute(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            sender.addChatMessage(new ChatComponentText("用法：/mcbot setBot <机器人QQ号>"));
            return;
        }
        final long id;
        try {
            id = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText("机器人QQ号必须是十进制数字。"));
            return;
        }
        ConfigHandler.cached().getCommon().setBotId(id);
        ConfigHandler.cached().getBotConfig().setBotId(id);
        sender.addChatMessage(
                new ChatComponentText("已设置机器人QQ号为:" + id));
        ConfigHandler.save();

    }


}
