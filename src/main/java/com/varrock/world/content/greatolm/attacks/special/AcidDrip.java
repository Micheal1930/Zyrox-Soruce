package com.varrock.world.content.greatolm.attacks.special;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.*;
import com.varrock.util.Misc;
import com.varrock.world.content.CustomObjects;
import com.varrock.world.content.greatolm.OlmAnimations;
import com.varrock.world.content.greatolm.RaidsParty;
import com.varrock.world.entity.impl.player.Player;

public class AcidDrip {

    public static void performAttack(RaidsParty party, int height) {
        party.getGreatOlmNpc().performGreatOlmAttack(party);
        party.setOlmAttackTimer(6);

        TaskManager.submit(new Task(1, party, true) {
            int tick = 0;

            @Override
            public void execute() {
                if (party.getGreatOlmNpc().isDying() || party.isSwitchingPhases()) {
                    stop();
                }
                if (tick == 1) {
                    if (party.getPlayersInRaids().size() >= 1) {
                        int random = Misc.getRandom(party.getPlayersInRaids().size() - 1);
                        party.setAcidDripPlayer(party.getPlayers().get(random));
                        party.getAcidDripPlayer().sendMessage(
                                "@red@The Great Olm has smothered you in acid. It starts to drip off slowly.");
                    } else {
                        stop();
                    }
                }
                if (tick == 2) {
                    OlmAnimations.resetAnimation(party);
                }
                if (tick == 30) {
                    party.getDripPools().clear();
                    stop();
                }
                for (int iz = 0; iz < 30; iz++) {
                    if (party.getAcidDripPlayer() != null) {
                        if (tick == iz + 2) {
                            if (party.getAcidDripPlayer().isInsideRaids()) {
                                Position acidSpotPosition = party.getAcidDripPlayer().getPosition();
                                GameObject pool = new GameObject(130032, acidSpotPosition, GameObject.ObjectType.GROUND_OBJECT.getInteger(), Misc.getRandom(3));
                                CustomObjects.acidPool(pool, party.getOwner(), 1, 25);
                                party.getDripPools().add(acidSpotPosition);
                            }
                        }
                        if (tick == (2 * iz) + 3) {
                            for (Player member : party.getPlayers()) {
                                if (member != null && member.isInsideRaids()) {
                                    for (int i = 0; i < party.getDripPools().size(); i++) {
                                        if (member.getPosition().sameAs(party.getDripPools().get(i))) {
                                            member.dealDamage(new Hit(20 + Misc.getRandom(30),
                                                    Hitmask.DARK_GREEN, CombatIcon.NONE));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                tick++;
            }
        });

    }

}
