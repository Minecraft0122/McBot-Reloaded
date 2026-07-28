package cn.evole.mods.mcbot.api.cmd;

import net.minecraft.network.chat.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class McBotCommandSourceTest {
    @Test
    void separatesMultipleResponsesWithNewlines() {
        McBotCommandSource source = new McBotCommandSource(null);

        source.sendSystemMessage(Component.literal("第一行"));
        source.sendSystemMessage(Component.literal("第二行"));

        assertEquals("第一行\n第二行", source.getCommandResponse());
    }

    @Test
    void capturesDelayedCommandResponses() throws Exception {
        McBotCommandSource source = new McBotCommandSource(null);
        Thread response = new Thread(() -> {
            try {
                Thread.sleep(50);
                source.sendSystemMessage(Component.literal("异步结果"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        response.start();
        String result = source.awaitCommandResponse(300, 25, 500);
        response.join(500);

        assertFalse(response.isAlive());
        assertEquals("异步结果", result);
    }
}
