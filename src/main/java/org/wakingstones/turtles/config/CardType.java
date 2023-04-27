package org.wakingstones.turtles.config;

public class CardType {
    public String name, flavor, kind;
    public int attack, cooldown, hp, costDecay, costGrowth, costEnergy, costOrder, rarity, set;
    public String[] type, rules;
    public Tag[] tags;

    public static class Tag {
        public String key, value;
    }
}
