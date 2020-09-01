package org.wakingstones.turtles.mixins;

import battle.rule.RuleFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import platform.util.LogUtil;
import platform.util.function.ClassMapping;

@Mixin(RuleFactory.class)
public abstract class MixinBattleRules {

    @Shadow @Final private static ClassMapping ruleRegistry;

    @Inject(method = "<clinit>", at= @At("TAIL"))
    private static void onRuleLoad(CallbackInfo ci) {
        LogUtil.game.info("Registering custom rules");
        ruleRegistry.registerClassesIn(Package.getPackage("org/wakingstones/turtles/rules"));
    }
}
