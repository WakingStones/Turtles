package org.wakingstones.turtles.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import platform.model.entity.ProfileData;

@Mixin(ProfileData.class)
public abstract class MixinProfileData {

    @ModifyConstant(remap = false, method = "getTopRanked")
    private static String fixGetTopRanked(String input) {
        return "SELECT P.name, PD.alpha_ranked * PD.real_ranked AS rating FROM profiles P INNER JOIN profile_data PD ON PD.profile_id = P.id WHERE p.admin_role != 'System' AND p.user_uuid NOT IN ('@limited', '@marketplace', 'Shopkeeper', 'Upgrade') ORDER BY rating DESC";
    }

    @ModifyConstant(remap = false, method = "getTopRankedProfiles")
    private static String fixGetTopRankedProfiles(String input) {
        return "SELECT p.* FROM profile_data PD INNER JOIN profiles p ON p.id = PD.profile_id WHERE p.admin_role != 'System' AND p.user_uuid NOT IN ('@limited', '@marketplace', 'Shopkeeper', 'Upgrade') ORDER BY (pd.alpha_ranked * pd.real_ranked) DESC";
    }
}
