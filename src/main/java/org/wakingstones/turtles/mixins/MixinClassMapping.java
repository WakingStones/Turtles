package org.wakingstones.turtles.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import platform.util.LogUtil;
import platform.util.function.ClassMapping;
import platform.util.function.IClassProvider;

@Mixin(ClassMapping.class)
public abstract class MixinClassMapping implements IClassProvider {

    @Inject(method="registerClass", at=@At("HEAD"), remap = false)
    public void onRegisterClass(Class<?> cl, CallbackInfo ci) {
        if (cl != null) {
            LogUtil.game.info("Registering rule class: " + cl.getSimpleName());
        }
    }
}
