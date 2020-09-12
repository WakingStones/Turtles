package org.wakingstones.turtles.rules;

import battle.Unit;
import battle.effect.Effect;
import battle.rule.enchantment.EnchantmentRule;
import battle.rule.modifier.Modifier;
import battle.rule.modifier.actions.AddSubType;
import battle.rule.modifier.atoms.IncHp;
import battle.rule.modifier.targets.atoms.Self;
import battle.update.SelectableTiles;
import platform.model.entity.card.CardType;

import java.util.Collections;
import java.util.List;

public class Appurtnance_2 extends EnchantmentRule implements CustomRule {

    public SelectableTiles selectableTiles() {
        return allCreaturesSel();
    }

    protected List<Effect> enchant(Unit unit) {
        add(new IncHp(unit, player().unitFilter().subType(CardType.SubType.Beast).count()));
        add(new Modifier((new Self()).init(unit), new AddSubType(CardType.SubType.Beast)));
        return Collections.emptyList();
    }
}