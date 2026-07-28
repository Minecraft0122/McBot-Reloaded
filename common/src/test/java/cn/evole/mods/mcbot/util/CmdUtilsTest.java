package cn.evole.mods.mcbot.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CmdUtilsTest {
    @Test
    void replacesNamedAndPositionalVariables() {
        String withGroup = CmdUtils.innerVarParse("mcbot addBind %group_id% %", Map.of("group_id", "123"));

        assertEquals("mcbot addBind 123 Steve", CmdUtils.outerVarParse(withGroup, List.of("Steve")));
        assertEquals("say 你好 世界", CmdUtils.outerVarParse("say % %", List.of("你好", "世界")));
        assertEquals("say 你好 世界", CmdUtils.outerVarParse("say %", List.of("你好", "世界")));
    }
}
