package cn.evole.mods.mcbot;

import cn.evole.mods.mcbot.api.cmd.McBotCommandSource;
import cn.evole.mods.mcbot.util.FileUtils;
import cn.evole.onebot.client.OneBotClient;
import cn.evole.onebot.sdk.util.GsonUtils;
import com.google.gson.Gson;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Constants {

    public static final String MOD_ID = "mcbot";
    public static final String MOD_NAME = "McBot";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static volatile ExecutorService msgExecutor = Executors.newCachedThreadPool();
    public static volatile ExecutorService cqExecutor = Executors.newSingleThreadExecutor();
    public static volatile ExecutorService commonExecutor = Executors.newFixedThreadPool(4);
    public static final Gson GSON = GsonUtils.getNullGson();
    public static final Path CONFIG_FOLDER = FileUtils.checkFolder(PlatformHelper.getGamePath().resolve("mcbot"));
    public static final Path DATA_FOLDER = FileUtils.checkFolder(CONFIG_FOLDER.resolve("data"));

    public static volatile boolean isShutdown = false;
    public static volatile boolean connected = false;

    public static volatile OneBotClient onebot;
    public static volatile MinecraftServer SERVER = null;
    public static volatile McBotCommandSource mcBotCommand = null;

    public static synchronized void startExecutors() {
        if (msgExecutor.isShutdown()) msgExecutor = Executors.newCachedThreadPool();
        if (cqExecutor.isShutdown()) cqExecutor = Executors.newSingleThreadExecutor();
        if (commonExecutor.isShutdown()) commonExecutor = Executors.newFixedThreadPool(4);
    }

    public static void shutdown(){
        cqExecutor.shutdownNow();
        msgExecutor.shutdownNow();
        commonExecutor.shutdown();
        try {
            if (!commonExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                LOGGER.warn("等待数据任务结束超时，将强制关闭数据线程池。");
                commonExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            commonExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
