package org.wakingstones.turtles.mixins;

import battle.rule.RuleFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.wakingstones.turtles.rules.CustomRule;
import platform.util.function.ClassMapping;

@Mixin(RuleFactory.class)
public abstract class MixinBattleRules {

    @Shadow @Final private static ClassMapping ruleRegistry;

    @Inject(method = "<clinit>", at= @At("TAIL"))
    private static void onRuleLoad(CallbackInfo ci) {
        ruleRegistry.registerClassesIn(CustomRule.class.getPackage());
    }
}
