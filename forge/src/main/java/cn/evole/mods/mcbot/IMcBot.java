package cn.evole.mods.mcbot;

import cn.evole.mods.mcbot.config.ModConfig;
import cn.evole.mods.mcbot.core.data.ChatRecordApi;
import cn.evole.mods.mcbot.core.data.UserBindApi;
import cn.evole.mods.mcbot.core.event.*;
import cn.evole.mods.mcbot.init.handler.CustomCmdHandler;
import cn.evole.mods.mcbot.util.FileUtil;
import cn.evole.mods.mcbot.util.locale.I18n;
import cn.evole.mods.mcbot.util.onebot.CQUtils;
import cn.evole.mods.mcbot.util.onebot.KeepAlive;
import cn.evole.onebot.client.OneBotClient;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.nio.file.Path;

public class IMcBot {
    public static MinecraftServer SERVER = null;
    public static Path CONFIG_FOLDER;
    public static Path CONFIG_FILE;

    public static OneBotClient onebot;

    public static boolean connected = false;
    public static KeepAlive keepAlive;

    public MinecraftServer getServer() {
        return SERVER;
    }

    public IMcBot() {
        init();
    }

    public void init() {
        CONFIG_FOLDER = Const.gameDir.resolve("mcbot");
        FileUtil.checkFolder(CONFIG_FOLDER);
        CONFIG_FILE = CONFIG_FOLDER.resolve("config.toml");
        I18n.init();
        UserBindApi.load(CONFIG_FOLDER);
        ChatRecordApi.load(CONFIG_FOLDER);
    }

    public void onServerStarting(MinecraftServer server) {
        SERVER = server;//获取服务器实例
    }

    public void onServerStarted(MinecraftServer server) {
        ModConfig.INSTANCE.save();
        if (ModConfig.INSTANCE.getCommon().isAutoOpen()) {
            onebot = OneBotClient.create(ModConfig.INSTANCE.getBotConfig().build()).open().registerEvents(new IBotEvent());
            connected = true;
        }
        CustomCmdHandler.INSTANCE.load();//自定义命令加载
        keepAlive = new KeepAlive();
        Const.messageThread.register(keepAlive::register);//自动重连注册
    }

    public void onServerStopping(MinecraftServer server) {
        Const.isShutdown = true;
        Const.LOGGER.info("▌ §c正在关闭群服互联");
        connected = false;
        if (onebot != null) {
            try {
                onebot.close();
            } catch (RuntimeException e) {
                Const.LOGGER.warn("关闭 OneBot WebSocket 时发生异常", e);
            } finally {
                onebot = null;
            }
        }
        Const.shutdown();//消息线程关闭
        CQUtils.shutdown();//cq转义线程关闭
        UserBindApi.save(CONFIG_FOLDER);
        ChatRecordApi.save(CONFIG_FOLDER);
        CustomCmdHandler.INSTANCE.clear();//自定义命令持久层清空
    }

    public void onServerStopped(MinecraftServer server) {
        // 连接与线程已在 SERVER_STOPPING 阶段关闭，防止重连线程阻止进程退出。
    }

    public void onServerTick(MinecraftServer server) {
        ITickEvent.register(server);
    }
    public void onServerChat(Level level, ServerPlayer player, String msg) {
        IChatEvent.register(player, msg);
    }
    public void onPlayerLogIn(Level level, ServerPlayer player) {
        IPlayerEvent.loggedIn(level, player);
    }
    public void onPlayerLogOut(Level level, ServerPlayer player) {
        IPlayerEvent.loggedOut(level, player);
    }
    public void onPlayerDeath(Level level, DamageSource source, ServerPlayer player) {
        IPlayerEvent.death(source, player);
    }
    public void onPlayerAdvancement(Level level, Player player, Advancement advancement) {
        IPlayerEvent.advancement(player, advancement);
    }
}
