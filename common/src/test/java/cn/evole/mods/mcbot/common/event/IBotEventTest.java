package cn.evole.mods.mcbot.common.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IBotEventTest {
    @Test
    void choosesFirstUsableDisplayName() {
        assertEquals("群名片", IBotEvent.firstNonBlank("群名片", "昵称", "10000"));
        assertEquals("昵称", IBotEvent.firstNonBlank("", "昵称", "10000"));
        assertEquals("10000", IBotEvent.firstNonBlank("null", null, "10000"));
    }
}
