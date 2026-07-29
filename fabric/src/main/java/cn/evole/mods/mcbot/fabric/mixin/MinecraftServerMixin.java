package cn.evole.mods.mcbot.fabric.mixin;

import cn.evole.mods.mcbot.McBot;
import cn.evole.mods.mcbot.common.event.ITickEvent;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

/**
 * 使用 Minecraft 原生生命周期注入点，避免要求用户安装 Fabric API。
 */
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;initServer()Z"))
    private void mcbot$beforeServerStart(CallbackInfo ci) {
        McBot.onServerStarting((MinecraftServer) (Object) this);
    }

    @Inject(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;buildServerStatus()Lnet/minecraft/network/protocol/status/ServerStatus;", ordinal = 0))
    private void mcbot$afterServerStart(CallbackInfo ci) {
        McBot.onServerStarted((MinecraftServer) (Object) this);
    }

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void mcbot$beforeServerStop(CallbackInfo ci) {
        McBot.onServerStopping((MinecraftServer) (Object) this);
    }

    @Inject(method = "stopServer", at = @At("TAIL"))
    private void mcbot$afterServerStop(CallbackInfo ci) {
        McBot.onServerStopped((MinecraftServer) (Object) this);
    }

    @Inject(method = "tickServer", at = @At("TAIL"))
    private void mcbot$afterServerTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ITickEvent.register((MinecraftServer) (Object) this);
    }
}
