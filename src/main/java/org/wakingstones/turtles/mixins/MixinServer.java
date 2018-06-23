package org.wakingstones.turtles.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(platform.Server.class)
public class MixinServer {

    @Inject(method="main", at = @At("HEAD"), remap = false)
    private static void main (String[] args, CallbackInfo ci) {
        System.out.println("Mixin hooks are confirmed to be loadable on the server now");
    }
}
