package cn.evole.mods.mcbot.api.bot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BotApiTest {
    @Test
    void splitsLongCommandOutputAtLineBoundaries() {
        var parts = BotApi.splitGroupText("第一行\n第二行很长\n第三行", 8);

        assertEquals("第一行\n", parts.get(0));
        assertTrue(parts.stream().allMatch(part -> part.length() <= 8));
        assertEquals("第一行\n第二行很长\n第三行", String.join("", parts));
    }
}
