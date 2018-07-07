package org.wakingstones.turtles.core.challenges;

import platform.model.entity.Profile;
import platform.model.entity.customgame.CustomGameEntity;

public class GameChallenge {
    private final Profile from, to;
    private final String deck, setupCode;
    private final CustomGameEntity entity;
    private final long creationTime = System.currentTimeMillis();

    public GameChallenge(Profile from, Profile to, String deck, String setupCode, CustomGameEntity entity) {
        this.from = from;
        this.to = to;
        this.deck = deck;
        this.setupCode = setupCode;
        this.entity = entity;
    }

    public Profile getFrom() {
        return from;
    }

    public Profile getTo() {
        return to;
    }

    public String getDeck() {
        return deck;
    }

    public String getSetupCode() {
        return setupCode;
    }

    public CustomGameEntity getEntity() {
        return entity;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public boolean hasExpired(long timeout) {
        return System.currentTimeMillis() - creationTime > timeout;
    }
}