package org.wakingstones.turtles.mixins;

import battle.rule.RuleFactory;
import com.google.gson.Gson;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.wakingstones.turtles.config.CardType;
import org.wakingstones.turtles.config.RuleDescription;
import platform.model.entity.Dao;
import platform.model.entity.card.RuleDescEntity;
import platform.util.LogUtil;

import javax.persistence.Query;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Mixin(platform.model.entity.card.CardType.class)
public abstract class MixinCardType {

    private static boolean hasRanUpdates = false;
    private static final Object locker = new Object();

    @Inject(method = "all", at = @At("HEAD"))
    private static void all(CallbackInfoReturnable ci) {
        hook$runCardUpdates();
    }

    @Inject(method = "allPublicNoCache", at = @At("HEAD"))
    private static void allPublicNoCache(CallbackInfoReturnable ci) {
        hook$runCardUpdates();
    }

    private static void hook$runCardUpdates() {
        synchronized (locker) {
            if (hasRanUpdates) {
                return;
            }

            CardType[] cards;
            try (FileReader reader = new FileReader("card_types.json")) {
                cards = new Gson().fromJson(reader, CardType[].class);
            } catch (FileNotFoundException e) {
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Query query = Dao.entityManager().createNativeQuery("UPDATE `card_types` SET" +
                    " `rules` = :rules, `type` = :type, `ap` = :attack, `ac` = :cooldown, `hp` = :hp, `tags` = :tags, " +
                    " `cost_decay` = :decay, `cost_energy` = :energy, `cost_order` = :order, `cost_growth` = :growth" +
                    " WHERE `name` = :name");

            for (CardType card : cards) {
                LogUtil.game.info("Updating " + card.name + " based on file changes");

                String tags = Arrays.stream(card.tags == null ? new CardType.Tag[0] : card.tags).map(t -> t.key + "=" + t.value).collect(Collectors.joining("\n"));
                String rules = String.join(",", card.rules == null ? new String[0] : card.rules);
                String types = String.join(",", card.type == null ? new String[0] : card.type);

                if (tags.equals("")) {
                    tags = null;
                }

                if (types.equals("")) {
                    rules = null;
                }

                if (types.equals("")) {
                    types = null;
                }

                query.setParameter("rules", rules);
                query.setParameter("type", types);
                query.setParameter("tags", tags);
                query.setParameter("attack", card.attack);
                query.setParameter("cooldown", card.cooldown);
                query.setParameter("hp", card.hp);

                query.setParameter("decay", card.costDecay);
                query.setParameter("growth", card.costGrowth);
                query.setParameter("energy", card.costEnergy);
                query.setParameter("order", card.costOrder);

                query.setParameter("name", card.name);

                LogUtil.game.info(query.toString());

                query.executeUpdate();
            }

            hasRanUpdates = true;
        }
    }
}

