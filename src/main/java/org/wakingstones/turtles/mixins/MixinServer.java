package org.wakingstones.turtles.mixins;

import org.objectweb.asm.ClassReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.util.asm.ASM;
import platform.model.entity.card.RuleDescEntity;
import platform.util.LogUtil;

@Mixin(platform.Server.class)
public abstract class MixinServer {

    @Inject(method="main", at = @At("HEAD"), remap = false)
    private static void main (String[] args, CallbackInfo ci) {
        LogUtil.game.info("Mixin hooks are confirmed to be loadable on the server now");
    }
}
