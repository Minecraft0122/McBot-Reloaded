package cn.evole.mods.mcbot.util;

import cn.evole.mods.mcbot.api.event.mod.McBotEvents;
import cn.evole.onebot.client.OneBotClient;
import cn.evole.onebot.sdk.action.misc.ActionPath;
import com.google.gson.JsonObject;
import lombok.val;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import static cn.evole.mods.mcbot.Constants.*;

/**
 * @Project: McBot
 * @Author: cnlimiter
 * @CreateTime: 2024/8/12 01:28
 * @Description:
 */
public class MsgThreadUtils {
    public static final MsgThreadUtils INSTANCE = new MsgThreadUtils();

    public void submit(long groupId, String msg, boolean autoEscape) {
        if (msg == null || msg.isBlank()) {
            LOGGER.debug("已忽略发往群 {} 的空消息", groupId);
            return;
        }
        LOGGER.debug("转发游戏消息: {}", msg);
        submitTask(() -> {
            try {
                OneBotClient client = onebot;
                if (client == null) return;
                client.getBot().sendGroupMsg(groupId, msg, autoEscape);
            } catch (Exception e) {
                LOGGER.error("向群 {} 转发消息失败", groupId, e);
            }
        });
    }

    public void submit(long groupId, Callable<String> msg, boolean autoEscape) {
        submitTask(() -> {
            try {
                val message = msg.call();
                if (message == null || message.isBlank()) {
                    LOGGER.debug("已忽略发往群 {} 的空消息", groupId);
                    return;
                }
                LOGGER.debug("转发游戏消息: {}", message);
                OneBotClient client = onebot;
                if (client != null) client.getBot().sendGroupMsg(groupId, message, autoEscape);
            } catch (Exception e) {
                LOGGER.error("向群 {} 转发消息失败", groupId, e);
            }
        });
    }

    public void submit(long groupId, Callable<String> msg, boolean autoEscape, ServerPlayer player) {
        submitTask(() -> {
            try {
                val message = msg.call();
                if (message == null || message.isBlank()) {
                    LOGGER.debug("已忽略发往群 {} 的空消息", groupId);
                    return;
                }
                LOGGER.debug("转发游戏消息: {}", message);
                OneBotClient client = onebot;
                if (client == null) return;
                McBotEvents.ON_CHAT.invoker().onChat(player,
                        client.getBot().sendGroupMsg(groupId, message, autoEscape).getData().getMessageId(),
                        message
                );
            } catch (Exception e) {
                LOGGER.error("向群 {} 转发玩家消息失败", groupId, e);
            }
        });
    }

    public void submit(ActionPath action, JsonObject params) {
        LOGGER.info("执行自定义操作：{}", action);
        submitTask(() -> {
            try {
                OneBotClient client = onebot;
                if (client != null) client.getBot().customRequest(action, params);
            } catch (Exception e) {
                LOGGER.error("执行 OneBot 自定义操作 {} 失败", action, e);
            }
        });
    }

    private void submitTask(Runnable task) {
        ExecutorService executor = msgExecutor;
        if (isShutdown || executor.isShutdown()) return;
        try {
            executor.submit(task);
        } catch (RejectedExecutionException e) {
            LOGGER.debug("消息线程池已关闭，忽略新任务");
        }
    }
}
