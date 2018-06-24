package org.wakingstones.turtles.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import platform.message.AuthenticationNotRequired;
import platform.message.Message;
import platform.message.ReceivableMessage;
import platform.message.account.RegisterAccount;

@Mixin(RegisterAccount.class)
public abstract class MixinRegisterAccount extends ReceivableMessage implements AuthenticationNotRequired {

    @Shadow(remap = false)
    String username;

    @Inject(method="handle", at = @At("HEAD"), remap = false, cancellable = true)
    public void handle(CallbackInfoReturnable<Message> ret) {
        if(!username.matches("[a-zA-Z0-9_]+")) {
            ret.setReturnValue(this.fail("Username can only contain letters, numbers, and _"));
            ret.cancel();
        }
    }
}
