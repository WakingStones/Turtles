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

    @Inject(method="registerClass", at=@At("HEAD"), remap = false)
    public void onRegisterClass(Class<?> cl, CallbackInfo ci) {
        if (cl != null) {
            LogUtil.game.info("Registering rule class: " + cl.getName());
            if (cl != CustomRule.class && CustomRule.class.isAssignableFrom(cl)) {
                try {
                    CustomRule rule = (CustomRule) cl.newInstance();
                    RuleDescEntity entity = RuleDescEntity.findByName(rule.name());
                    //if it's null from the finder, then we need to create it
                    if (entity != null) {
                        return;
                    }

                    entity = new RuleDescEntity();

                    try {
                        idField.set(entity, rule.name());
                        displayNameField.set(entity, rule.defaultDisplayName());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException((e));
                    }

                    entity.description = rule.defaultDescription();
                    entity.isPassive = rule.defaultIsPassive();
                    entity.save();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
