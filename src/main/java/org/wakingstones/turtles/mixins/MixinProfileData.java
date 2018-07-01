package org.wakingstones.turtles.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import platform.model.entity.ProfileData;

@Mixin(ProfileData.class)
public abstract class MixinProfileData {

    @ModifyConstant(remap = false, method = "getTopRanked")
    private static String fixGetTopRanked(String input) {
        return input + " WHERE p.admin_role != 'System' AND p.user_uuid NOT IN ('@limited', '@marketplace', 'Shopkeeper', 'Upgrade')";
    }

    @ModifyConstant(remap = false, method = "getTopRankedProfiles")
    private static String fixGetTopRankedProfiles(String input) {
        return input + " WHERE p.admin_role != 'System' AND p.user_uuid NOT IN ('@limited', '@marketplace', 'Shopkeeper', 'Upgrade')";
    }
}
