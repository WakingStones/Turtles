package org.wakingstones.turtles.mixins;

import battle.Game;
import battle.setup.function.SetupMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.wakingstones.turtles.core.challenges.GameChallenge;
import platform.message.challenge.GameChallengeResponse;
import platform.message.cluster.BattleRedirect;
import platform.model.entity.Profile;
import platform.model.entity.Server;
import platform.model.entity.customgame.CustomGameEntity;
import platform.model.entity.setup.SetupPipeEntity;
import platform.service.ChallengeService;
import platform.service.ClusterService;
import platform.service.MessageCenter;

import java.util.HashMap;
import java.util.Map;

@Mixin(ChallengeService.class)
public class MixinChallengeService {

    private static final Map<Integer, GameChallenge> customChallenges = new HashMap<>();
    private static final long TIMEOUT = 1000 * 60 * 5; //5 minutes

    /**
     * @author Joshua Taylor (Lord_Ralex)
     */
    @Overwrite
    public static void createChallenge(Profile from, Profile to, String deck, String setupCode, CustomGameEntity cge) {
        synchronized (customChallenges) {
            //is there a challenge by the challenged for this challenger?
            if (customChallenges.get(to.id()) != null) {
                GameChallenge c = customChallenges.remove(to.id());

                MessageCenter.post(GameChallengeResponse.accept(from, to), from);
                MessageCenter.post(GameChallengeResponse.accept(to, from), to);

                SetupPipeEntity spe = SetupPipeEntity.find(Game.GameType.MP_UNRANKED.toString().toLowerCase());
                SetupMap sm = new SetupMap(c.getSetupCode());
                String error = sm.getBet().getErrors(from, to);

                if (error == null || error.isEmpty()) {
                    Server server = ClusterService.prepare(Game.GameType.MP_UNRANKED, from, to, c.getDeck(), deck, spe, c.getSetupCode());
                    MessageCenter.post(new BattleRedirect(server.ip(), server.port), from, to);
                }
            } else {
                customChallenges.put(from.id(), new GameChallenge(from, to, deck, setupCode, cge));
            }
        }
    }

    @Inject(method = "timeoutExpiredChallenges", at = @At("HEAD"), remap = false)
    private static void timeoutExpiredChallenges(CallbackInfo cb) {
        synchronized (customChallenges) {
            customChallenges.entrySet().removeIf((e) -> e.getValue().hasExpired(TIMEOUT));
        }
    }
}
