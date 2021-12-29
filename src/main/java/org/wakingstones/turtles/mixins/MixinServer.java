package org.wakingstones.turtles.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import platform.util.LogUtil;

@Mixin(value = platform.Server.class, remap = false)
public abstract class MixinServer {

    @Inject(method = "main", at = @At("HEAD"), remap = false)
    private static void onMain(String[] args, CallbackInfo ci) {
        LogUtil.game.info("Mixin hooks are confirmed to be loadable on the server now");
    }
}
