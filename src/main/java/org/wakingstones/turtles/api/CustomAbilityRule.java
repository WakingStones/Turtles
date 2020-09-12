package org.wakingstones.turtles.api;

import battle.rule.ability.AbilityRule;

public abstract class CustomAbilityRule extends AbilityRule {

    public static class DisplayInfo extends AbilityRule.DisplayInfo {
        public DisplayInfo(String name, String description) {
            super(name, description);
        }
    }
}



