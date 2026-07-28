package cn.evole.mods.mcbot.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinecraftTextUtilsTest {
    @Test
    void removesLegacyAndHexFormatting() {
        assertEquals("红色 普通", MinecraftTextUtils.sanitizeForOneBot("§c红色§r 普通"));
        assertEquals("彩色", MinecraftTextUtils.sanitizeForOneBot("§x§1§2§3§4§5§6彩色"));
    }

    @Test
    void removesAnsiAndControlCharactersButKeepsLayout() {
        assertEquals("第一行\n第二行\t内容",
                MinecraftTextUtils.sanitizeForOneBot("\u001B[31m第一行\u001B[0m\n第二行\u0000\t内容"));
    }
}
