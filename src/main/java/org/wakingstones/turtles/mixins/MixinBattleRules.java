package org.wakingstones.turtles.mixins;

import battle.rule.RuleFactory;
import com.google.gson.Gson;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.wakingstones.turtles.config.RuleDescription;
import org.wakingstones.turtles.rules.CustomRule;
import platform.model.entity.Dao;
import platform.model.entity.card.RuleDescEntity;
import platform.util.LogUtil;
import platform.util.function.ClassMapping;

import javax.persistence.Query;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

@Mixin(RuleFactory.class)
public abstract class MixinBattleRules {

    @Shadow
    @Final
    private static ClassMapping ruleRegistry;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addNewRules(CallbackInfo ci) {
        ruleRegistry.registerClassesIn(CustomRule.class.getPackage());

        RuleDescription[] rules;
        try (FileReader reader = new FileReader("rule_descs.json")) {
            rules = new Gson().fromJson(reader, RuleDescription[].class);
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final Field displayNameField;
        try {
            displayNameField = RuleDescEntity.class.getDeclaredField("displayName");
            displayNameField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        Query query = Dao.entityManager().createNativeQuery("UPDATE rule_descs SET displayName = :display, description = :desc, isPassive = :passive WHERE name = :name");

        for (RuleDescription rule : rules) {
            RuleDescEntity rde = RuleDescEntity.findByName(rule.name);
            if (rde != null) {
                LogUtil.game.info("Updating " + rule.name + " based on file changes");
                rde.isPassive = rule.isPassive;
                rde.description = rule.description;

                if (rule.displayName != null && rule.displayName.equals("")) {
                    rule.displayName = null;
                }

                try {
                    displayNameField.set(rde, rule.displayName);
                } catch (IllegalAccessException e) {
                    LogUtil.game.error("Failed to update display name", e);
                    //throw new RuntimeException(e);
                }

                //trigger a save so the internal stuff has it updated
                rde.save();

                query.setParameter("display", rule.displayName);
                query.setParameter("desc", rde.description);
                query.setParameter("passive", rde.isPassive);
                query.setParameter("name", rde.name());
                query.executeUpdate();
            }
        }
    }
}

