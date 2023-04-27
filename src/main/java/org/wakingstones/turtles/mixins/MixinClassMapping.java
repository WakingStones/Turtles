package org.wakingstones.turtles.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.wakingstones.turtles.rules.CustomRule;
import platform.model.entity.card.RuleDescEntity;
import platform.util.LogUtil;
import platform.util.function.ClassMapping;
import platform.util.function.IClassProvider;

import java.lang.reflect.Field;

@Mixin(ClassMapping.class)
public abstract class MixinClassMapping implements IClassProvider {

    private static final Field idField, displayNameField;

    static {
        try {
            idField = RuleDescEntity.class.getDeclaredField("name");
            displayNameField = RuleDescEntity.class.getDeclaredField("displayName");
            idField.setAccessible(true);
            displayNameField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Inject(method = "registerClass", at = @At("HEAD"), remap = false)
    public void onRegisterClass(Class<?> cl, CallbackInfo ci) {
        if (cl != null) {
            if (cl != CustomRule.class && CustomRule.class.isAssignableFrom(cl)) {
                LogUtil.game.info("Registering rule class: " + cl.getName());
                try {
                    CustomRule rule = (CustomRule) cl.newInstance();
                    //if it's null from the finder, then we need to create it
                    if (RuleDescEntity.findByName(rule.name()) == null) {
                        RuleDescEntity rde = new RuleDescEntity();

                        try {
                            idField.set(rde, rule.name());
                            displayNameField.set(rde, rule.defaultDisplayName());
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException((e));
                        }

                        rde.description = rule.defaultDescription();
                        rde.isPassive = rule.defaultIsPassive();
                        rde.save();
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
