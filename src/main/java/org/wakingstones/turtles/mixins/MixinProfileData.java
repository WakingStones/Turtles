package org.wakingstones.turtles.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import platform.model.entity.BaseEntity;
import platform.model.entity.ProfileData;
import platform.model.entity.ProfileRating;

import java.util.List;

@Mixin(ProfileData.class)
public abstract class MixinProfileData extends BaseEntity<Integer> {

    @ModifyConstant(remap = false, method = "getTopRanked")
    private static String fixSql(String input) {
        return input + " WHERE p.admin_role != 'System' AND p.user_uuid NOT IN ('@limited', '@miarketplace', 'Shopkeeper', 'Upgrade')";
    }

}
