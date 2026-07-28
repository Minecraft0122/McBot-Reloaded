package cn.evole.mods.mcbot.common.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BotConfigTest {
    @Test
    void preservesSecureWebSocketUrls() {
        assertEquals("wss://bot.example.com:443/onebot",
                BotConfig.normalizeWebSocketUrl("  WSS://bot.example.com:443/onebot  "));
    }

    @Test
    void addsDefaultWebSocketScheme() {
        assertEquals("ws://127.0.0.1:3001",
                BotConfig.normalizeWebSocketUrl("127.0.0.1:3001"));
    }

    @Test
    void rejectsUnsupportedSchemes() {
        assertThrows(IllegalArgumentException.class,
                () -> BotConfig.normalizeWebSocketUrl("http://127.0.0.1:3001"));
    }
}
