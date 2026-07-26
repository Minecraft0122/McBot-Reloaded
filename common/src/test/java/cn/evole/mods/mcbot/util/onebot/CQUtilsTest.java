package cn.evole.mods.mcbot.util.onebot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CQUtilsTest {
    @Test
    void fallsBackToQqWhenAtNameIsMissing() {
        assertEquals("[@全体]", CQUtils.formatAt("all", null));
        assertEquals("[@群友]", CQUtils.formatAt("10000", "群友"));
        assertEquals("[@10000]", CQUtils.formatAt("10000", "null"));
        assertEquals("[@]", CQUtils.formatAt(null, null));
    }
}
