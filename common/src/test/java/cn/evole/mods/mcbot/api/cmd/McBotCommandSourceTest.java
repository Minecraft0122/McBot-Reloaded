package cn.evole.mods.mcbot.api.cmd;

import net.minecraft.network.chat.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class McBotCommandSourceTest {
    @Test
    void separatesMultipleResponsesWithNewlines() {
        McBotCommandSource source = new McBotCommandSource(null);

        source.sendSystemMessage(Component.literal("第一行"));
        source.sendSystemMessage(Component.literal("第二行"));

        assertEquals("第一行\n第二行", source.getCommandResponse());
    }
}
