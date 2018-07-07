package org.wakingstones.turtles.mixins;

import com.mojang.commons.system.globalization.Now;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import platform.model.entity.Profile;
import platform.model.entity.marketplace.MarketplaceTypeAvailability;
import platform.service.marketplace.MarketplaceOfferFinder;

import java.util.List;

@Mixin(MarketplaceOfferFinder.class)
public abstract class MixinMarketplaceOfferFinder {

    @ModifyConstant(remap = false, method = "findAvailable")
    private static String fixFindAvailable(String input) {
        return "SELECT min(moo.price) as price, min(moo.level) as level, moo.type FROM ( SELECT ca.level AS LEVEL, mo.price AS price, ca.type_id AS TYPE FROM marketplace_offers mo LEFT JOIN cards ca ON ca.id = mo.card_id WHERE mo.seller_id != ? AND(mo.expires_at IS NULL OR mo.expires_at > ?) ) AS moo group by moo.type";
    }
}
