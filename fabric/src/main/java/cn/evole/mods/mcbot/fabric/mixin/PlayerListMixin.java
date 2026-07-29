package cn.evole.mods.mcbot.fabric.mixin;

import cn.evole.mods.mcbot.api.event.server.ServerGameEvents;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** 使用原版玩家列表生命周期转发登录和退出事件。 */
@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void mcbot$afterPlayerJoin(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        ServerGameEvents.PLAYER_LOGGED_IN.invoker().onPlayerLoggedIn(player.getServer(), player);
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void mcbot$beforePlayerLeave(ServerPlayer player, CallbackInfo ci) {
        ServerGameEvents.PLAYER_LOGGED_OUT.invoker().onPlayerLoggedOut(player.getServer(), player);
    }
}
