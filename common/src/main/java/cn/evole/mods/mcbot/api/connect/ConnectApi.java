package cn.evole.mods.mcbot.api.connect;

import cn.evole.mods.mcbot.Constants;
import cn.evole.mods.mcbot.common.config.ModConfig;
import cn.evole.mods.mcbot.common.event.IBotEvent;
import cn.evole.onebot.client.OneBotClient;

/**
 * @Project: McBot
 * @Author: cnlimiter
 * @CreateTime: 2024/8/12 01:49
 * @Description:
 */
public class ConnectApi {
    private ConnectApi() {
    }

    public static synchronized boolean wsConnect() {
        wsDisconnect();

        OneBotClient client = null;
        try {
            client = OneBotClient.create(ModConfig.get().getBotConfig().build());
            client.open().registerEvents(new IBotEvent());
            Constants.onebot = client;
            Constants.connected = true;
            ModConfig.get().getStatus().getSEnable().setValue(true);
            ModConfig.get().getCommon().getEnable().setValue(true);
            return true;
        } catch (Exception e) {
            Constants.LOGGER.error("连接 OneBot WebSocket 失败", e);
            closeQuietly(client);
            Constants.onebot = null;
            Constants.connected = false;
            return false;
        }
    }

    public static synchronized boolean wsDisconnect() {
        OneBotClient client = Constants.onebot;
        Constants.onebot = null;
        Constants.connected = false;
        if (client == null) return false;

        closeQuietly(client);
        return true;
    }

    public static boolean isConnected() {
        OneBotClient client = Constants.onebot;
        try {
            return client != null && client.getWs() != null && client.getWs().isOpen();
        } catch (RuntimeException e) {
            Constants.LOGGER.debug("读取 OneBot WebSocket 状态失败", e);
            return false;
        }
    }

    private static void closeQuietly(OneBotClient client) {
        if (client == null) return;
        try {
            client.close();
        } catch (Exception e) {
            Constants.LOGGER.warn("关闭 OneBot WebSocket 时发生异常", e);
        }
    }
}
