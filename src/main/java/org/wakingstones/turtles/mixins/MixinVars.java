package org.wakingstones.turtles.mixins;

import com.google.gson.Gson;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.wakingstones.turtles.config.RuleDescription;
import org.wakingstones.turtles.config.Var;
import platform.model.entity.Dao;
import platform.model.entity.vars.VarEntity;
import platform.model.entity.vars.Vars;
import platform.util.LogUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Mixin(Vars.class)
public abstract class MixinVars {

    @Inject(method = "test", at = @At("TAIL"))
    private static void updateVars(CallbackInfo ci) {
        Var[] vars;
        try (FileReader reader = new FileReader("vars.json")) {
            vars = new Gson().fromJson(reader, Var[].class);
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Var var : vars) {
            LogUtil.game.info("Updating " + var.key);
            Dao.entityManager().createNativeQuery("UPDATE `vars` SET `value`=? WHERE  `key`=?").setParameter(1, var.value).setParameter(2, var.key).executeUpdate();
        }
    }
}
