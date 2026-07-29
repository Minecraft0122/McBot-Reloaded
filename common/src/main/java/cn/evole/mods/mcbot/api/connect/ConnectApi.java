package cn.evole.mods.mcbot.api.connect;

import cn.evole.mods.mcbot.Constants;
import cn.evole.mods.mcbot.common.config.ModConfig;
import cn.evole.mods.mcbot.common.event.IBotEvent;
import cn.evole.onebot.client.OneBotClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;

/** 管理 OneBot WebSocket 的异步连接生命周期。 */
public final class ConnectApi {
    private static final Object STATE_LOCK = new Object();
    private static CompletableFuture<Boolean> activeAttempt;
    private static OneBotClient connectingClient;
    private static long generation;

    private ConnectApi() {
    }

    /**
     * 在独立守护线程中建立连接，避免网络超时阻塞 Minecraft 服务器线程。
     * 同一时间只允许一个连接尝试。
     */
    public static CompletableFuture<Boolean> wsConnectAsync() {
        synchronized (STATE_LOCK) {
            if (Constants.isShutdown) return CompletableFuture.completedFuture(false);
            if (isConnected()) return CompletableFuture.completedFuture(true);
            if (activeAttempt != null && !activeAttempt.isDone()) return activeAttempt;

            long attemptGeneration = ++generation;
            try {
                activeAttempt = CompletableFuture.supplyAsync(
                        () -> connect(attemptGeneration), Constants.connectExecutor);
            } catch (RejectedExecutionException e) {
                Constants.LOGGER.warn("OneBot 连接线程池已关闭，忽略本次连接请求。", e);
                activeAttempt = CompletableFuture.completedFuture(false);
            }
            return activeAttempt;
        }
    }

    private static boolean connect(long attemptGeneration) {
        OneBotClient previous;
        synchronized (STATE_LOCK) {
            if (attemptGeneration != generation || Constants.isShutdown) return false;
            previous = Constants.onebot;
            Constants.onebot = null;
            Constants.connected = false;
        }
        closeQuietly(previous);

        OneBotClient client = null;
        try {
            client = OneBotClient.create(ModConfig.get().getBotConfig().build());
            boolean accepted;
            synchronized (STATE_LOCK) {
                accepted = attemptGeneration == generation && !Constants.isShutdown;
                if (accepted) connectingClient = client;
            }
            if (!accepted) {
                closeQuietly(client);
                return false;
            }

            client.open().registerEvents(new IBotEvent());

            boolean keepConnection;
            synchronized (STATE_LOCK) {
                keepConnection = attemptGeneration == generation && !Constants.isShutdown;
                if (keepConnection) {
                    Constants.onebot = client;
                    Constants.connected = true;
                    connectingClient = null;
                    ModConfig.get().getStatus().getSEnable().setValue(true);
                    ModConfig.get().getCommon().getEnable().setValue(true);
                }
            }
            if (!keepConnection) closeQuietly(client);
            return keepConnection;
        } catch (Exception e) {
            boolean currentAttempt;
            synchronized (STATE_LOCK) {
                currentAttempt = attemptGeneration == generation;
                if (connectingClient == client) connectingClient = null;
                if (currentAttempt) {
                    Constants.onebot = null;
                    Constants.connected = false;
                }
            }
            if (currentAttempt && !Constants.isShutdown) {
                Constants.LOGGER.error("连接 OneBot WebSocket 失败", e);
            }
            closeQuietly(client);
            return false;
        } finally {
            synchronized (STATE_LOCK) {
                if (connectingClient == client) connectingClient = null;
            }
        }
    }

    public static boolean wsDisconnect() {
        CompletableFuture<Boolean> attempt;
        OneBotClient current;
        OneBotClient connecting;
        synchronized (STATE_LOCK) {
            generation++;
            attempt = activeAttempt;
            activeAttempt = null;
            current = Constants.onebot;
            connecting = connectingClient;
            connectingClient = null;
            Constants.onebot = null;
            Constants.connected = false;
        }

        boolean hadPendingAttempt = attempt != null && !attempt.isDone();
        if (hadPendingAttempt) attempt.cancel(true);
        closeQuietly(connecting);
        if (current != connecting) closeQuietly(current);
        return current != null || connecting != null || hadPendingAttempt;
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
            if (client.getWs() != null) {
                // 普通 close() 不会取消 OneBot Client 的自动重连 Timer，会阻止服务端 JVM 退出。
                client.getWs().stopWithoutReconnect(1000, "McBot disconnect");
            }
            client.close();
        } catch (Exception e) {
            Constants.LOGGER.warn("关闭 OneBot WebSocket 时发生异常", e);
        }
    }
}
