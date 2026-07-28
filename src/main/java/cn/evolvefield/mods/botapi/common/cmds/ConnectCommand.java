package cn.evolvefield.mods.botapi.common.cmds;


import cn.evolvefield.mods.botapi.BotApi;
import cn.evolvefield.mods.botapi.Const;
import cn.evolvefield.mods.botapi.init.handler.ConfigHandler;
import cn.evolvefield.onebot.client.config.BotConfig;
import com.mojang.realmsclient.gui.ChatFormatting;
import lombok.val;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ConnectCommand {

    public static void cqhttpExecute(ICommandSender sender, String[] args) throws CommandException {
        val parameter = args[2];


        try {
            ConfigHandler.cached().getBotConfig().setUrl(BotConfig.normalizeWebSocketUrl(parameter));
            sender.addChatMessage(new ChatComponentText("尝试链接框架" + ChatFormatting.LIGHT_PURPLE + "cqhttp"));
            ConfigHandler.cached().getBotConfig().setMiraiHttp(false);

            BotApi.connectAsync();
            ConfigHandler.cached().getStatus().setRECEIVE_ENABLED(true);
            ConfigHandler.cached().getCommon().setEnable(true);
            ConfigHandler.save();



        } catch (IllegalArgumentException e) {
            sender.addChatMessage(new ChatComponentText(ChatFormatting.RED + e.getMessage()));
        }
    }

    public static void miraiExecute(ICommandSender sender, String[] args) throws CommandException {
        val parameter = args[2];

        try {
            ConfigHandler.cached().getBotConfig().setUrl(BotConfig.normalizeWebSocketUrl(parameter));
            sender.addChatMessage(new ChatComponentText("尝试链接框架" + ChatFormatting.LIGHT_PURPLE + "mirai"));
            ConfigHandler.cached().getBotConfig().setMiraiHttp(true);
            BotApi.connectAsync();
            ConfigHandler.cached().getStatus().setRECEIVE_ENABLED(true);
            ConfigHandler.cached().getCommon().setEnable(true);
            ConfigHandler.save();



        } catch (IllegalArgumentException e) {
            sender.addChatMessage(new ChatComponentText(ChatFormatting.RED + e.getMessage()));
        }
    }

    public static void cqhttpCommonExecute(ICommandSender sender, String[] args) throws CommandException {

        sender.addChatMessage(new ChatComponentText("尝试链接框架" + ChatFormatting.LIGHT_PURPLE + "cqhttp"));
        ConfigHandler.cached().getBotConfig().setMiraiHttp(false);
        BotApi.connectAsync();
        ConfigHandler.cached().getStatus().setRECEIVE_ENABLED(true);
        ConfigHandler.cached().getCommon().setEnable(true);
        ConfigHandler.save();


    }

    public static void miraiCommonExecute(ICommandSender sender, String[] args) throws CommandException {


        sender.addChatMessage(new ChatComponentText("尝试链接框架" + ChatFormatting.LIGHT_PURPLE + "mirai"));
        ConfigHandler.cached().getBotConfig().setMiraiHttp(true);
        BotApi.connectAsync();
        ConfigHandler.cached().getStatus().setRECEIVE_ENABLED(true);
        ConfigHandler.cached().getCommon().setEnable(true);

        ConfigHandler.save();


    }

}
