package cn.evole.mods.mcbot.common.event;

import cn.evole.mods.mcbot.api.bot.BotApi;
import cn.evole.mods.mcbot.common.config.ModConfig;
import cn.evole.mods.mcbot.util.MinecraftTextUtils;
import cn.evole.onebot.sdk.util.MsgUtils;
import lombok.val;
import net.minecraft.server.level.ServerPlayer;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/1/18 10:37
 * Version: 1.0
 */
public class IChatEvent {
    public static void register(ServerPlayer player, String message) {
        String cleanMessage = MinecraftTextUtils.sanitizeForOneBot(message);
        val split = cleanMessage.split(" ", 2);
        if (
                ModConfig.get().getStatus().getSChatEnable().getValue()
                        && ModConfig.get().getStatus().getSEnable().getValue()
                && !cleanMessage.contains("CICode")
                        && !player.getCommandSenderWorld().isClientSide
        ) {
            String msg = String.format(ModConfig.get().getCmd().getMcPrefixOn().getValue()
                            ? "[" + ModConfig.get().getCmd().getMcPrefix().getValue() + "]<%s> %s"
                            : "<%s> %s",
                    MinecraftTextUtils.sanitizeForOneBot(player.getDisplayName().getString()),
                    ModConfig.get().getCmd().getMcChatPrefixOn().getValue()
                            && split.length == 2
                            && ModConfig.get().getCmd().getMcChatPrefix().getValue().equals(split[0]) ? split[1] : cleanMessage);

            BotApi.sendAllGroupMsg(() -> MsgUtils.builder().text(msg).build(), player);
        }
    }

}
