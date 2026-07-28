package cn.evole.mods.mcbot.util;

import java.util.regex.Pattern;

/** 将 Minecraft 文本转发到 OneBot 前移除不兼容的格式控制码。 */
public final class MinecraftTextUtils {
    private static final Pattern LEGACY_FORMATTING = Pattern.compile(
            "(?i)§x(?:§[0-9a-f]){6}|§[0-9a-fk-or]");
    private static final Pattern ANSI_FORMATTING = Pattern.compile("\\u001B\\[[;\\d]*m");

    private MinecraftTextUtils() {
    }

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
