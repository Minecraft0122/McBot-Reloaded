package cn.evole.mods.mcbot.util.locale;

import cn.evole.mods.mcbot.Constants;
import cn.evole.mods.mcbot.PlatformHelper;
import cn.evole.mods.mcbot.common.config.ModConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.locale.Language;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * @Project: McBot
 * @Author: cnlimiter
 * @CreateTime: 2024/8/15 20:01
 * @Description:
 */
public class I18n {
    private static final Gson GSON = new Gson();
    private static volatile Map<String, String> translations = Map.of();

    public static void init() {
        Optional<Path> optional = PlatformHelper.getResourcePath("lang/" + ModConfig.get().getCommon().getLanguageSelect().getValue() + ".json");

        if (optional.isEmpty()) {
            Constants.LOGGER.warn("-----------------------------------------");
            Constants.LOGGER.warn("找不到语言文件“{}”，将使用简体中文。", ModConfig.get().getCommon().getLanguageSelect().getValue());
            Constants.LOGGER.warn("");
            Constants.LOGGER.warn("欢迎向项目贡献翻译：https://github.com/Minecraft0122/McBot-Reloaded");
            Constants.LOGGER.warn("-----------------------------------------");

            optional = PlatformHelper.getResourcePath("lang/zh_cn.json");
        }

        if (optional.isPresent()) {
            try {
                String content = Files.readString(optional.get(), StandardCharsets.UTF_8);
                Map<String, String> loaded = GSON.fromJson(content, new TypeToken<Map<String, String>>() {
                }.getType());
                translations = loaded == null ? Map.of() : Map.copyOf(loaded);
            } catch (Exception e) {
                translations = Map.of();
                Constants.LOGGER.error("加载语言文件失败", e);
            }
        } else {
            translations = Map.of();
        }
    }

    public static void reload() {
        init();
    }

    public static String get(String key, Object... args) {
        try {
            String translation1 = translations.get(key);
            if (translation1 != null) {
                return String.format(translation1, args);
            } else {
                String key2 = key.replaceAll("mcbot.", "");
                String translation2 = Language.getInstance().getOrDefault(key2);
                if (!translation2.equals(key2)) {
                    return String.format(translation2, args);
                } else {
                    return "翻译错误{\"键\":\"" + key2 + "\",\"参数\":" + Arrays.toString(args) + "}";
                }
            }
        } catch (Exception e) {
            return "翻译错误{\"键\":\"" + key + "\",\"参数\":" + Arrays.toString(args) + "}";
        }
    }

    public static String get(String key) {
        String translation = translations.get(key);
        if (translation != null) {
            return translation;
        } else {
            return key;
        }
    }
}
