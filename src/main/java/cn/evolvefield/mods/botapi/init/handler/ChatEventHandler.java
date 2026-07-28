package cn.evolvefield.mods.botapi.init.handler;

import cn.evolvefield.mods.botapi.BotApi;
import cn.evolvefield.mods.botapi.util.MinecraftTextUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import lombok.val;
import net.minecraftforge.event.ServerChatEvent;



public class ChatEventHandler {
    public static ChatEventHandler INSTANCE = new ChatEventHandler();
    private boolean initialized;

    public synchronized void preInit() {
        if (initialized) return;
        FMLCommonHandler.instance().bus().register(this);
        initialized = true;
    }

    @SubscribeEvent
    public void onChatEvent(ServerChatEvent event) {
        val message = MinecraftTextUtils.sanitizeForOneBot(event.message);
        val player = event.player;
        val split = message.split(" ", 2);
        if (ConfigHandler.cached() != null
                && BotApi.bot != null
                && ConfigHandler.cached().getStatus().isS_CHAT_ENABLE()
                && ConfigHandler.cached().getStatus().isSEND_ENABLED()
                && !message.contains("CICode")
        ) {
            if (ConfigHandler.cached().getCommon().isGuildOn() && !ConfigHandler.cached().getCommon().getChannelIdList().isEmpty()) {
                for (String id : ConfigHandler.cached().getCommon().getChannelIdList())
                    BotApi.bot.sendGuildMsg(ConfigHandler.cached().getCommon().getGuildId(),
                            id,
                            String.format("[" + ConfigHandler.cached().getCmd().getMcPrefix() + "]<%s> %s",
                                    player.getDisplayName(),
                                    ConfigHandler.cached().getCmd().isMcChatPrefixEnable()
                                            && split.length == 2
                                            && ConfigHandler.cached().getCmd().getMcChatPrefix().equals(split[0]) ? split[1] : message));
            } else {
                for (long id : ConfigHandler.cached().getCommon().getGroupIdList())
                    BotApi.bot.sendGroupMsg(
                            id,
                            String.format("[" + ConfigHandler.cached().getCmd().getMcPrefix() + "]<%s> %s",
                                    player.getDisplayName(),
                                    ConfigHandler.cached().getCmd().isMcChatPrefixEnable()
                                            && split.length == 2
                                            && ConfigHandler.cached().getCmd().getMcChatPrefix().equals(split[0]) ? split[1] : message),
                            true);
            }


        }
    }
}
