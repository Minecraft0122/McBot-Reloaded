package cn.evolvefield.mods.botapi.common.cmds;

import cn.evolvefield.mods.botapi.init.handler.ConfigHandler;
import lombok.val;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class AddGroupIDCommand {


    public static void execute(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            sender.addChatMessage(new ChatComponentText("用法：/mcbot addGroup <QQ群号>"));
            return;
        }
        final long id;
        try {
            id = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText("QQ群号必须是十进制数字。"));
            return;
        }
        if (ConfigHandler.cached().getCommon().getGroupIdList().contains(id)) {
            sender.addChatMessage(new ChatComponentText("QQ群号:" + id + "已经出现了！"));
        } else {
            ConfigHandler.cached().getCommon().addGroupId(id);
            sender.addChatMessage(new ChatComponentText("已成功添加QQ群号:" + id + "！"));
        }
        ConfigHandler.save();
    }


}
