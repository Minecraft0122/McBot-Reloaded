package cn.evole.mods.mcbot.forge;

import cn.evole.mods.mcbot.common.config.ModConfig;
import com.iafenvoy.jupiter.render.screen.ConfigSelectScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @Project: McBot
 * @Author: cnlimiter
 * @CreateTime: 2024/10/27 03:21
 * @Description:
 */
public final class McBotForgeClient {
    private McBotForgeClient() {
    }

    public static void register(FMLJavaModLoadingContext context) {
        context.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, screen) ->
                                new ConfigSelectScreen<>(Component.translatable("config.mcbot.title"), screen, ModConfig.INSTANCE, null)));
    }

}
