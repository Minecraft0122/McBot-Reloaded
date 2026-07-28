package cn.evole.mods.mcbot.api.cmd;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

/**
 * @Project: McBot
 * @Author: cnlimiter
 * @CreateTime: 2024/8/11 20:52
 * @Description:
 */
public class McBotCommandSource implements CommandSource {
    private static final Component MCBOT_COMPONENT = Component.literal("McBot");
    private static final long INITIAL_ASYNC_WAIT_MILLIS = 1500;
    private static final long RESPONSE_SETTLE_MILLIS = 200;
    private static final long MAX_ASYNC_WAIT_MILLIS = 5000;
    private final StringBuffer buffer = new StringBuffer();
    private final MinecraftServer server;
    private long responseRevision;
    private long lastResponseNanos;

    public McBotCommandSource(MinecraftServer server) {
        this.server = server;
    }

    public synchronized void prepareForCommand() {
        this.buffer.setLength(0);
        this.responseRevision = 0;
        this.lastResponseNanos = 0;
    }

    public synchronized String getCommandResponse() {
        return this.buffer.toString();
    }

    public CommandSourceStack createCommandSourceStack() {
        ServerLevel overworld = this.server.overworld();
        return new CommandSourceStack(this, Vec3.atLowerCornerOf(overworld.getSharedSpawnPos()), Vec2.ZERO, overworld, 4, "McBot", MCBOT_COMPONENT, this.server, null);
    }

    @Override
    public synchronized void sendSystemMessage(Component component) {
        if (!this.buffer.isEmpty()) this.buffer.append('\n');
        this.buffer.append(component.getString());
        this.responseRevision++;
        this.lastResponseNanos = System.nanoTime();
        this.notifyAll();
    }

    @Override
    public boolean acceptsSuccess() {
        return true;
    }

    @Override
    public boolean acceptsFailure() {
        return true;
    }

    @Override
    public boolean shouldInformAdmins() {
        return false;
    }

    public synchronized String runCommand(String cmd) {
        McBotCommandSource invocation = new McBotCommandSource(this.server);
        server.executeBlocking(() -> server.getCommands().performPrefixedCommand(invocation.createCommandSourceStack(), cmd));
        return invocation.awaitCommandResponse(INITIAL_ASYNC_WAIT_MILLIS, RESPONSE_SETTLE_MILLIS, MAX_ASYNC_WAIT_MILLIS);
    }

    synchronized String awaitCommandResponse(long initialWaitMillis, long settleMillis, long maxWaitMillis) {
        if (initialWaitMillis < 0 || settleMillis < 0 || maxWaitMillis < initialWaitMillis) {
            throw new IllegalArgumentException("命令响应等待参数无效");
        }

        long started = System.nanoTime();
        long initialDeadline = started + initialWaitMillis * 1_000_000L;
        long maximumDeadline = started + maxWaitMillis * 1_000_000L;
        while (true) {
            long now = System.nanoTime();
            long deadline = responseRevision == 0
                    ? initialDeadline
                    : Math.min(maximumDeadline, lastResponseNanos + settleMillis * 1_000_000L);
            long remainingNanos = deadline - now;
            if (remainingNanos <= 0) break;

            try {
                long waitMillis = Math.max(1, (remainingNanos + 999_999L) / 1_000_000L);
                this.wait(waitMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return this.buffer.toString();
    }

}
