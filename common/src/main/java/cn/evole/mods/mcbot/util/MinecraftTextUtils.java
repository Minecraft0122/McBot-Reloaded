package cn.evole.mods.mcbot.util;

import java.util.regex.Pattern;

/** Minecraft 文本发送到外部聊天平台前的兼容性清理。 */
public final class MinecraftTextUtils {
    private static final Pattern LEGACY_FORMATTING = Pattern.compile(
            "(?i)§x(?:§[0-9a-f]){6}|§[0-9a-fk-or]");
    private static final Pattern ANSI_FORMATTING = Pattern.compile("\\u001B\\[[;\\d]*m");

    private MinecraftTextUtils() {
    }

    /**
     * 移除 FTB 等模组可能写入聊天文本的 Minecraft/ANSI 颜色码和不可见控制字符，
     * 同时保留换行、回车与制表符。
     */
    public static String sanitizeForOneBot(String text) {
        if (text == null || text.isEmpty()) return "";

        String sanitized = ANSI_FORMATTING.matcher(
                LEGACY_FORMATTING.matcher(text).replaceAll("")).replaceAll("");
        StringBuilder result = new StringBuilder(sanitized.length());
        for (int i = 0; i < sanitized.length(); i++) {
            char character = sanitized.charAt(i);
            if (character == '\n' || character == '\r' || character == '\t'
                    || !Character.isISOControl(character)) {
                result.append(character);
            }
        }
        return result.toString();
    }
}
