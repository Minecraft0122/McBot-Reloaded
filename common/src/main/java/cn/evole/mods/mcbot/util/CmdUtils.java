package cn.evole.mods.mcbot.util;

import cn.evole.mods.mcbot.api.cmd.Cmd;
import cn.evole.mods.mcbot.api.data.UserInfoApi;
import cn.evole.mods.mcbot.common.config.ModConfig;
import cn.evole.mods.mcbot.plugins.cmd.CmdHandler;
import cn.evole.onebot.sdk.event.message.GroupMessageEvent;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project: McBot
 * @Author: cnlimiter
 * @CreateTime: 2024/8/16 23:38
 * @Description:
 */
public class CmdUtils {

    public static boolean hasPermission(String group_id, String user_id, Cmd cmd){
        if (cmd == null) return false;
        if (cmd.getId().equals("bind") || "ALL".equalsIgnoreCase(cmd.getPermission())) return true;
        val userInfo = UserInfoApi.get(group_id, user_id);
        return userInfo != null && userInfo.getPermissions().contains(ModConfig.get().getBotConfig().getTag().getValue() + ".mcbot.cmd." + cmd.getId())
                || cmd.getAllow_members() != null && cmd.getAllow_members().contains(user_id);
    }

    /**
     *
     * @param event 消息事件
     * @return 是否是管理员
     */
    public static boolean groupAdminParse(GroupMessageEvent event) {
        String role = event.getSender().getRole();
        return "admin".equalsIgnoreCase(role) || "owner".equalsIgnoreCase(role);
    }

    /**
     * 变量解析
     *
     * @param event 消息事件
     * @param cmd   q群中指令
     * @return 处理完的指令
     */
    public static Cmd varParse(GroupMessageEvent event, String cmd) {
        Map<String, String> variables = new HashMap<>();
        variables.put("user_id", event.getSender().getUserId());//初始化变量列表
        variables.put("group_id", String.valueOf(event.getGroupId()));
        variables.put("user_age", String.valueOf(event.getSender().getAge()));
        variables.put("user_nickname", String.valueOf(event.getSender().getNickname()));

        String trimmedCmd = cmd.trim();
        if (trimmedCmd.isEmpty()) return null;
        String cmdStart = trimmedCmd.split("\\s+", 2)[0];//部分指令头

        if (cmdStart.isEmpty()) return null;

        Cmd selectCmd = null;
        for (Cmd cmd2 : CmdHandler.cmds.values()){//将含有昵称的指令替换为源命令
            if (cmd2.getCmd() == null || cmd2.getCmd().isBlank()) continue;
            String sourceCmd = cmd2.getCmd().trim().split("\\s+", 2)[0];
            if (cmd2.getId().equals(cmdStart) || sourceCmd.equals(cmdStart) || cmd2.getAlies() != null && cmd2.getAlies().contains(cmdStart)) {
                selectCmd = cmd2;
                break;//跳出循环
            }
        }

        if (selectCmd != null) {
            String innerParse = innerVarParse(selectCmd.getCmd(), variables);
            List<String> cmdSplits = new ArrayList<>(Arrays.asList(trimmedCmd.split("\\s+")));//拆分命令
            cmdSplits.remove(0);//移除命令名或别名，只保留参数
            String outerParse = outerVarParse(innerParse, cmdSplits);
            //拼接回命令
            return new Cmd(selectCmd.getId(), outerParse, selectCmd.getAlies(), selectCmd.getAllow_members(), selectCmd.getPermission(), selectCmd.getAfter_cmds(), selectCmd.getAnswer(), selectCmd.isEnable());
        } else {
            return null;//如果最终命令为空则返回null
        }
    }

    public static String innerVarParse(String command, Map<String, String> variables) {
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            command = command.replace("%" + key + "%", value);
        }
        return command;
    }

    public static String outerVarParse(String command, List<String> values) {
        StringBuilder result = new StringBuilder();
        int valueIndex = 0;

        for (int i = 0; i < command.length(); i++) {
            char currentChar = command.charAt(i);

            if (currentChar == '%') {
                if (valueIndex < values.size()) {
                    boolean lastPlaceholder = command.indexOf('%', i + 1) < 0;
                    if (lastPlaceholder) {
                        result.append(String.join(" ", values.subList(valueIndex, values.size())));
                        valueIndex = values.size();
                    } else {
                        result.append(values.get(valueIndex));
                        valueIndex++;
                    }
                }
            } else {
                result.append(currentChar);
            }
        }
        return result.toString();
    }
}
