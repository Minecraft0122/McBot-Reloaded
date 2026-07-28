package cn.evole.mods.mcbot.api.bot;

import cn.evole.mods.mcbot.common.config.ModConfig;
import cn.evole.mods.mcbot.common.event.ITickEvent;
import cn.evole.mods.mcbot.util.MsgThreadUtils;
import cn.evole.onebot.sdk.action.misc.ActionPath;
import com.google.gson.JsonObject;
import lombok.val;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.Callable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project: McBot
 * @Author: cnlimiter
 * @CreateTime: 2024/8/12 01:34
 * @Description:
 */
public class BotApi {
    private static final int MAX_GROUP_TEXT_LENGTH = 3500;

    public static void sendGroupMsg(long group_id, String message) {
        MsgThreadUtils.INSTANCE.submit(group_id, message, false);
    }

    public static void sendGroupMsg(long group_id, Callable<String> message) {
        MsgThreadUtils.INSTANCE.submit(group_id, message, false);
    }

    /**
     * 发送来自服务器命令的纯文本。自动转义可避免方括号、健康报告和
     * Spark 输出被 OneBot 当作不完整 CQ 码；过长内容会按行分段。
     */
    public static void sendGroupText(long groupId, String message) {
        for (String part : splitGroupText(message, MAX_GROUP_TEXT_LENGTH)) {
            MsgThreadUtils.INSTANCE.submit(groupId, part, true);
        }
    }

    static List<String> splitGroupText(String message, int maximumLength) {
        List<String> parts = new ArrayList<>();
        if (message == null || message.isBlank()) return parts;
        if (maximumLength < 1) throw new IllegalArgumentException("消息分段长度必须大于零");

        int start = 0;
        while (start < message.length()) {
            int end = Math.min(message.length(), start + maximumLength);
            if (end < message.length()) {
                int newline = message.lastIndexOf('\n', end - 1);
                if (newline >= start) end = newline + 1;
            }
            parts.add(message.substring(start, end));
            start = end;
        }
        return parts;
    }

    public static void sendAllGroupMsg(String message) {
        for (String id : ModConfig.get().getCommon().getGroupIdList().getValue()) {
            sendGroupMsg(Long.parseLong(id), message);
        }
    }

    public static void sendAllGroupMsg(Callable<String> message) {
        for (String id : ModConfig.get().getCommon().getGroupIdList().getValue()) {
            sendGroupMsg(Long.parseLong(id), message);
        }
    }

    /**
     * 玩家在游戏里发送消息
     *
     * @param message 消息
     * @param player  玩家
     */
    public static void sendAllGroupMsg(Callable<String> message, ServerPlayer player) {
        for (String id : ModConfig.get().getCommon().getGroupIdList().getValue()) {
            MsgThreadUtils.INSTANCE.submit(Long.parseLong(id), message, false, player);
        }
    }

    /**
     * 向游戏中的所有人发送消息
     */
    public static void sendAllPlayerMsg(String message) {
        val toSend = Component.literal(message);
        ITickEvent.sendQueue().add(toSend);
    }

    /**
     * 自定义请求 (不应清理)
     *
     * @param action 请求类型
     * @param params 参数
     */
    public static void customRequest(ActionPath action, JsonObject params) {
        MsgThreadUtils.INSTANCE.submit(action, params);
    }

}
