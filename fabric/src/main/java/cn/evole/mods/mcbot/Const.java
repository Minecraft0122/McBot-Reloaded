package cn.evole.mods.mcbot;

import cn.evole.mods.mcbot.config.ConfigManager;
import cn.evole.mods.mcbot.core.event.IBotEvent;
import cn.evole.mods.mcbot.core.event.ITickEvent;
import cn.evole.mods.mcbot.util.onebot.MessageThread;
import cn.evole.onebot.client.OneBotClient;
import cn.evole.onebot.sdk.action.ActionPath;
import com.google.gson.JsonObject;
import lombok.val;
import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.concurrent.Callable;
import cn.evole.mods.mcbot.util.MinecraftTextUtils;
import net.minecraft.server.level.ServerPlayer;
//#if MC >= 11700
//$$ import org.slf4j.Logger;
//$$ import org.slf4j.LoggerFactory;
//#else
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
//#endif
import net.minecraft.network.chat.Component;
//#if MC < 11900
import net.minecraft.network.chat.TextComponent;
//#endif

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/1 16:58
 * Version: 1.0
 */
public class Const {
    public static final String MODID = "mcbot";
    //#if MC >= 11700
    //$$ public static final Logger LOGGER = LoggerFactory.getLogger("McBot");
    //#else
    public static final Logger LOGGER = LogManager.getLogger("McBot");
    //#endif
    public static boolean isShutdown = false;
    public static Path configDir = FabricLoader.getInstance().getConfigDir();
    public static Path gameDir = FabricLoader.getInstance().getGameDir();
    public static final MessageThread messageThread = new MessageThread();

    public static boolean isLoad(String modId){
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static void sendAllGroupMsg(String message){
        for (long id : new LinkedHashSet<Long>(ConfigManager.instance().getCommon().getGroupIdList())){
            sendGroupMsg(id, message);
        }
    }

    public static void sendAllGroupMsg(Callable<String> message){
        for (long id : new LinkedHashSet<Long>(ConfigManager.instance().getCommon().getGroupIdList())){
            sendGroupMsg(id, message);
        }
    }

    /**
     * 玩家在游戏里发送消息
     * @param message 消息
     * @param player 玩家
     */
    public static void sendAllGroupMsg(Callable<String> message, ServerPlayer player){
        for (long id : new LinkedHashSet<Long>(ConfigManager.instance().getCommon().getGroupIdList())){
            messageThread.submit(id, message, false, player);
        }
    }

    public static void sendGroupMsg(long id, String message){
        messageThread.submit(id, message, false);
    }

    public static void sendGroupMsg(long id, Callable<String> message){
        messageThread.submit(id, message, false);
    }

    public static void sendGroupText(long id, String message) {
        String cleanMessage = MinecraftTextUtils.sanitizeForOneBot(message);
        if (cleanMessage.trim().isEmpty()) return;

        int start = 0;
        while (start < cleanMessage.length()) {
            int end = Math.min(cleanMessage.length(), start + 3500);
            if (end < cleanMessage.length()) {
                int newline = cleanMessage.lastIndexOf('\n', end - 1);
                if (newline >= start) end = newline + 1;
            }
            messageThread.submit(id, cleanMessage.substring(start, end), true);
            start = end;
        }
    }

    /**
     * 自定义请求 (不应清理)
     * @param action 请求类型
     * @param params 参数
     */
    public static void customRequest(ActionPath action, JsonObject params){
        messageThread.submit(action, params);
    }

    /**
     * 向游戏中的所有人发送消息
     */
    public static void sendAllPlayerMsg(String message){
        //#if MC >= 11900
        //$$ val toSend = Component.literal(message);
        //#else
        val toSend = new TextComponent(message);
        //#endif

        ITickEvent.getSendQueue().add(toSend);
    }

    /**
     * WS连接
     */
    public static synchronized void wsConnect(){
        if (isShutdown) {
            throw new IllegalStateException("服务器正在关闭，不能建立 OneBot 连接");
        }

        OneBotClient previous = McBot.onebot;
        McBot.onebot = null;
        McBot.connected = false;
        if (previous != null) {
            try {
                previous.close();
            } catch (RuntimeException e) {
                LOGGER.warn("关闭旧 OneBot 连接时发生异常", e);
            }
        }

        OneBotClient client = OneBotClient.create(ConfigManager.instance().getBotConfig().build());
        try {
            client.open().registerEvents(new IBotEvent());
            McBot.onebot = client;
            ConfigManager.instance().getStatus().setREnable(true);
            ConfigManager.instance().getCommon().setEnable(true);
            McBot.connected = true;
        } catch (RuntimeException e) {
            try {
                client.close();
            } catch (RuntimeException closeError) {
                e.addSuppressed(closeError);
            }
            throw e;
        }
    }

    public static void wsConnectAsync() {
        messageThread.register(() -> {
            try {
                wsConnect();
            } catch (RuntimeException e) {
                if (!isShutdown) LOGGER.error("连接 OneBot 失败，可修正配置后使用连接命令重试", e);
            }
        });
    }


    public static void shutdown() {
        messageThread.stop();
    }
}
