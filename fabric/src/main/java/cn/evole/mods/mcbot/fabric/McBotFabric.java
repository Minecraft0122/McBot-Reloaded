package cn.evole.mods.mcbot.fabric;

import cn.evole.mods.mcbot.McBot;
import net.fabricmc.api.ModInitializer;

public class McBotFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        McBot.init();
    }
}
