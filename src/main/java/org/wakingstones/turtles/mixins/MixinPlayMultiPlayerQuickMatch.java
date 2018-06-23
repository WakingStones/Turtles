package org.wakingstones.turtles.mixins;

import battle.Game;
import org.spongepowered.asm.mixin.Mixin;
import platform.message.Message;
import platform.message.play.PlayMultiPlayerQuickMatch;
import platform.message.play.PlayRatingBasedGameMessage;

@Mixin(PlayMultiPlayerQuickMatch.class)
public abstract class MixinPlayMultiPlayerQuickMatch extends PlayRatingBasedGameMessage {

    protected MixinPlayMultiPlayerQuickMatch(Game.GameType gameType) {
        super(gameType);
    }

    @Override
    public Message handle() {
        return super.handle();
    }
}
