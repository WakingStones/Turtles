package org.wakingstones.turtles.rules;

public interface CustomRule {

    /**
     * Gets the name of this rule.
     * This will be the name of the rule in the DB, and matches the name of this class.
     *
     * @return Name of the rule
     */
    default String name() {
        return this.getClass().getSimpleName();
    }

    /**
     * Gets the default display name of this rule if the database does not have this rule.
     * By default, this will be null, as the database expects null.
     *
     * @return Display name
     */
    default String defaultDisplayName() {
        return null;
    }

    /**
     * Gets the default description of this rule.
     * Any placeholders should be left, so that the game fills them in.
     * @return Description of the rule
     */
    default String defaultDescription() {
        return "";
    }

    /**
     * Gets whether the rule defaults to be passive.
     *
     * @return True if passive, false otherwise
     */
    default boolean defaultIsPassive() {
        return false;
    }
}
