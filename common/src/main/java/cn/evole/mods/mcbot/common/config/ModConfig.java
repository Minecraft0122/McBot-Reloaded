package cn.evole.mods.mcbot.common.config;

import cn.evole.mods.mcbot.Constants;
import com.google.gson.JsonObject;
import com.iafenvoy.jupiter.config.container.AutoInitConfigContainer;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static cn.evole.mods.mcbot.Constants.CONFIG_FOLDER;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/2 13:44
 * Version: 1.0
 */

@Getter
@Setter
public class ModConfig extends AutoInitConfigContainer {
    public static final ModConfig INSTANCE = new ModConfig();
    public static final int CURRENT_VERSION = 1;

    public CommonConfig common = new CommonConfig();
    public StatusConfig status = new StatusConfig();
    public CmdConfig cmd = new CmdConfig();
    public BotConfig botConfig = new BotConfig();

    public ModConfig() {
        super(new ResourceLocation("config.mcbot"), "config.mcbot.title", "./mcbot/config.json");
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected boolean shouldLoad(JsonObject obj) {
        if (!obj.has("version") || !obj.get("version").isJsonPrimitive()) {
            Constants.LOGGER.warn("{} 配置缺少有效的版本号，将备份旧配置并生成新配置。", Constants.MOD_NAME);
            backupCurrentConfig();
            return false;
        }

        final int version;
        try {
            version = obj.get("version").getAsInt();
        } catch (RuntimeException e) {
            Constants.LOGGER.warn("{} 配置版本号无效，将备份旧配置并生成新配置。", Constants.MOD_NAME);
            backupCurrentConfig();
            return false;
        }

        if (version != CURRENT_VERSION && new File(this.path).exists()) {
            backupCurrentConfig();
            Constants.LOGGER.info("{} 配置版本不匹配：当前为 {}，需要 {}。旧配置已备份，将生成新配置。", Constants.MOD_NAME, version, CURRENT_VERSION);
            return false;
        } else Constants.LOGGER.info("{} 配置版本检查通过。", Constants.MOD_NAME);
        return true;
    }

    private void backupCurrentConfig() {
        File currentConfig = new File(this.path);
        if (!currentConfig.isFile()) return;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            File backup = new File(CONFIG_FOLDER + File.separator + "config_" + sdf.format(new Date()) + ".json");
            FileUtils.copyFile(currentConfig, backup);
        } catch (IOException e) {
            throw new IllegalStateException("无法备份旧配置文件", e);
        }
    }

    @Override
    protected void writeCustomData(JsonObject obj) {
        obj.addProperty("version", CURRENT_VERSION);
    }

    public static ModConfig get() {
        return INSTANCE;
    }

}
