package cn.evole.mods.mcbot.common.command;


import cn.evole.mods.mcbot.Constants;
import cn.evole.mods.mcbot.api.connect.ConnectApi;
import cn.evole.mods.mcbot.common.config.ModConfig;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.val;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class StatusCommand {

    public static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        boolean clientEnabled = ModConfig.get().getCommon().getEnable().getValue();

        boolean receiveEnabled = ModConfig.get().getStatus().getREnable().getValue();
        boolean rChatEnabled = ModConfig.get().getStatus().getRChatEnable().getValue();
        boolean rCmdEnabled = ModConfig.get().getStatus().getRCmdEnable().getValue();

        boolean sendEnabled = ModConfig.get().getStatus().getSEnable().getValue();
        boolean sJoinEnabled = ModConfig.get().getStatus().getSJoinEnable().getValue();
        boolean sLeaveEnabled = ModConfig.get().getStatus().getSLeaveEnable().getValue();
        boolean sDeathEnabled = ModConfig.get().getStatus().getSDeathEnable().getValue();
        boolean sAchievementsEnabled = ModConfig.get().getStatus().getSAdvanceEnable().getValue();
        boolean sQqWelcomeEnabled = ModConfig.get().getStatus().getSQqWelcomeEnable().getValue();
        boolean sQqLeaveEnabled = ModConfig.get().getStatus().getSQqLeaveEnable().getValue();

        val groupId = ModConfig.get().getCommon().getGroupIdList().getValue().toString();
        boolean debuggable = ModConfig.get().getCommon().getDebug().getValue();
        boolean connected = ConnectApi.isConnected();
        boolean white = Constants.SERVER.getPlayerList().isUsingWhitelist();
        String host = ModConfig.get().getBotConfig().getUrl().getValue();
        String QQid = ModConfig.get().getBotConfig().getBotId().getValue();
        String toSend =
                "\n机器人服务状态：\n"
                        + "机器人 QQ 号：" + QQid + " \n"
                        + "框架地址：" + host + " \n"
                        + "WebSocket 连接：" + state(connected) + "\n"
                        + "互通群号：" + groupId + "\n"
                        + "全局服务：" + state(clientEnabled) + "\n"
                        + "调试模式：" + state(debuggable) + "\n"
                        + "服务器白名单：" + state(white) + "\n"
                        + "*************************************\n"
                        + "接收消息：" + state(receiveEnabled) + "\n"
                        + "接收 QQ 群聊天：" + state(rChatEnabled) + "\n"
                        + "接收 QQ 群命令：" + state(rCmdEnabled) + "\n"
                        + "*************************************\n"
                        + "发送消息：" + state(sendEnabled) + "\n"
                        + "发送玩家加入消息：" + state(sJoinEnabled) + "\n"
                        + "发送玩家离开消息：" + state(sLeaveEnabled) + "\n"
                        + "发送玩家死亡消息：" + state(sDeathEnabled) + "\n"
                        + "发送玩家进度消息：" + state(sAchievementsEnabled) + "\n"
                        + "发送群成员入群消息：" + state(sQqWelcomeEnabled) + "\n"
                        + "发送群成员退群消息：" + state(sQqLeaveEnabled) + "\n";
        context.getSource().sendSuccess(() -> Component.literal(toSend), true);
        
        return 1;
    }

    private static String state(boolean enabled) {
        return enabled ? "已开启" : "已关闭";
    }
}
