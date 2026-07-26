package cn.evole.mods.mcbot.common.event;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/1/18 10:47
 * Version: 1.0
 */
public class ITickEvent {
    private static final Queue<MutableComponent> SEND_QUEUE = new ConcurrentLinkedQueue<>();

    public static Queue<MutableComponent> sendQueue() {
        return SEND_QUEUE;
    }

    public static void register(MinecraftServer server) {
        MutableComponent toSend = SEND_QUEUE.poll();
        if (server != null
                && server.isDedicatedServer()
                && toSend != null
        ) {
            server.getPlayerList().broadcastSystemMessage(toSend, false);
        }
    }
}
