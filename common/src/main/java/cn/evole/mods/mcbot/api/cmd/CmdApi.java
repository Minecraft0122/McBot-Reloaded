package cn.evole.mods.mcbot.api.cmd;

import cn.evole.mods.mcbot.Constants;
import cn.evole.mods.mcbot.api.bot.BotApi;
import cn.evole.mods.mcbot.common.config.ModConfig;
import cn.evole.mods.mcbot.plugins.cmd.CmdHandler;
import cn.evole.mods.mcbot.util.CmdUtils;
import cn.evole.onebot.sdk.event.message.GroupMessageEvent;
import lombok.val;

/**
 * @Project: McBot
 * @Author: cnlimiter
 * @CreateTime: 2024/8/12 01:56
 * @Description:
 */
public class CmdApi {


    public static void invokeGroupCommand(GroupMessageEvent event, String msg) {
        String prefix = ModConfig.get().getCmd().getCmdStart().getValue();
        if (!msg.startsWith(prefix)) return;
        String originCmd = msg.substring(prefix.length()).trim();//去除可配置的命令前缀

        val user_id = String.valueOf(event.getUserId());
        val group_id = String.valueOf(event.getGroupId());

        val cmd = CmdUtils.varParse(event, originCmd);

        if (cmd == null) return;
        Constants.LOGGER.info("QQ群 {} 的用户 {} 执行 McBot 命令：{}", group_id, user_id, cmd.getId());

        if (CmdUtils.groupAdminParse(event)) {
            runAndReply(event.getGroupId(), cmd);
            if (cmd.getAfter_cmds() != null && !cmd.getAfter_cmds().isEmpty()) {
                cmd.getAfter_cmds().forEach(s -> {
                    Cmd afterCommand = CmdHandler.cmds.get(s);
                    if (afterCommand == null) BotApi.sendGroupMsg(event.getGroupId(), Constants.mcBotCommand.runCommand(s));
                    else runAndReply(event.getGroupId(), afterCommand);
                });
            }
        } else if (CmdUtils.hasPermission(group_id, user_id, cmd)) {
            runAndReply(event.getGroupId(), cmd);
            if (cmd.getAfter_cmds() != null && !cmd.getAfter_cmds().isEmpty()) {//连续指令是否为空
                cmd.getAfter_cmds().forEach(s -> {
                    Cmd afterCommand = CmdHandler.cmds.get(s);
                    if (CmdUtils.hasPermission(group_id, user_id, afterCommand)) {//再次检测下条指令是否有权限
                        runAndReply(event.getGroupId(), afterCommand);
                    }
                });
            }
        }
    }

    private static void runAndReply(long groupId, Cmd cmd) {
        String response = Constants.mcBotCommand.runCommand(cmd.getCmd());
        if ((response == null || response.isBlank())
                && cmd.getAnswer() != null
                && !cmd.getAnswer().isBlank()
                && !"NO".equalsIgnoreCase(cmd.getAnswer())) {
            response = cmd.getAnswer();
        }
        Constants.LOGGER.info("McBot 命令 {} 执行完成，返回 {} 个字符。",
                cmd.getId(), response == null ? 0 : response.length());
        BotApi.sendGroupText(groupId, response);
    }
}
