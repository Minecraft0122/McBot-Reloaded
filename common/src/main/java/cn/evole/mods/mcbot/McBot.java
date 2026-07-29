package cn.evole.mods.mcbot;


import cn.evole.mods.mcbot.api.cmd.McBotCommandSource;
import cn.evole.mods.mcbot.api.connect.ConnectApi;
import cn.evole.mods.mcbot.api.event.server.ServerGameEvents;
import cn.evole.mods.mcbot.common.config.ModConfig;
import cn.evole.mods.mcbot.common.event.IChatEvent;
import cn.evole.mods.mcbot.common.event.IPlayerEvent;
import cn.evole.mods.mcbot.plugins.cmd.CmdHandler;
import cn.evole.mods.mcbot.plugins.data.DataHandler;
import cn.evole.mods.mcbot.util.locale.I18n;
import com.iafenvoy.jupiter.ConfigManager;
import com.iafenvoy.jupiter.ServerConfigManager;
import net.minecraft.server.MinecraftServer;

import static cn.evole.mods.mcbot.Constants.*;

public class McBot {
    private static boolean initialized;

    public static synchronized void init() {
        if (initialized) {
            LOGGER.debug("McBot 事件已注册，跳过重复初始化。");
            return;
        }

        LOGGER.info("McBot Java 运行环境：{}（{}；{}）",
                Runtime.version(),
                System.getProperty("java.vendor"),
                System.getProperty("java.vm.name"));

        try {
            ConfigManager.getInstance().registerConfigHandler(ModConfig.INSTANCE);
            ServerConfigManager.registerServerConfig(ModConfig.INSTANCE, ServerConfigManager.PermissionChecker.IS_OPERATOR);
        } catch (Exception e) {
            LOGGER.error("配置加载失败", e);
        }

        ServerGameEvents.PLAYER_LOGGED_IN.register((server, player) -> IPlayerEvent.loggedIn(player.level(), player));
        ServerGameEvents.PLAYER_LOGGED_OUT.register((server, player) -> IPlayerEvent.loggedOut(player.level(), player));
        ServerGameEvents.PLAYER_ADVANCEMENT.register(IPlayerEvent::advancement);
        ServerGameEvents.PLAYER_DEATH.register(IPlayerEvent::death);
        ServerGameEvents.SERVER_CHAT.register(IChatEvent::register);
        initialized = true;
    }


    public static void onServerStarting(MinecraftServer server) {
        Constants.startExecutors();
        isShutdown = false;
        SERVER = server;//获取服务器实例
        I18n.init();
        CmdHandler.load();//在接受消息前完成自定义命令加载
        DataHandler.load();//在接受消息前完成数据加载

    }

    public static void onServerStarted(MinecraftServer server) {
        mcBotCommand = new McBotCommandSource(server);
        if (ModConfig.get().getCommon().getAutoOpen().getValue()) {
            ConnectApi.wsConnectAsync();
        }
    }

    public static void onServerStopping(MinecraftServer server) {
        isShutdown = true;
        LOGGER.info("▌ §c正在关闭群服互联");
        ConnectApi.wsDisconnect();
        Constants.shutdown();
        CmdHandler.clear();//自定义命令持久层清空
        DataHandler.save();//异步数据操作结束后再保存
    }

    public static void onServerStopped(MinecraftServer server) {
        try {
            ModConfig.get().save();
        } finally {
            ConnectApi.wsDisconnect();
            SERVER = null;
            mcBotCommand = null;
        }
    }
}
