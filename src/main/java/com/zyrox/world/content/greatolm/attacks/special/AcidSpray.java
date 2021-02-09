package com.zyrox.world.content.greatolm.attacks.special;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.*;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.CustomObjects;
import com.zyrox.world.content.greatolm.OlmAnimations;
import com.zyrox.world.content.greatolm.RaidsParty;
import com.zyrox.world.content.greatolm.attacks.Attacks;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

public class AcidSpray {

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
                    for (int i = 0; i < 10; i++) {
                        Position acidSpotPosition = Attacks.randomLocation(height);
                        party.getAcidPoolsNpcs()[i] = NPC.of(5090, acidSpotPosition);

                        World.register(party.getAcidPoolsNpcs()[i]);
                        new Projectile(party.getGreatOlmNpc(), party.getAcidPoolsNpcs()[i],
                                Attacks.DARK_GREEN_SMALL_PROJECTILE, 60, 8, 70, 10, 0).sendProjectile();
                        // GameObject pool = new GameObject(30032, acidSpotPosition);
                        // GameObjects.acidPool(pool, party.getOwner(), 15);

                    }
                }
                if (tick == 2) {
                    OlmAnimations.resetAnimation(party);
                }
                if (tick == 3) {
                    for (int i = 0; i < 10; i++) {
                        Position acidSpotPosition = party.getAcidPoolsNpcs()[i].getPosition();
                        GameObject pool = new GameObject(130032, acidSpotPosition);
                        CustomObjects.spawnTempObject(pool, party.getOwner(), 15);
                    }
                }
                if (tick >= 2 && tick <= 18) {
                    for (Player member : party.getPlayers()) {
                        if (member != null && member.isInsideRaids()) {
                            for (int i = 0; i < 7; i++) {
                                if (member.getPosition().sameAs(party.getAcidPoolsNpcs()[i].getPosition())) {
                                    member.dealDamage(new Hit(
                                            30 + Misc.getRandom(30), Hitmask.DARK_GREEN, CombatIcon.NONE));
                                }
                            }
                        }
                    }
                }
                if (tick == 19) {
                    for (int i = 0; i < 10; i++) {
                        World.deregister(party.getAcidPoolsNpcs()[i]);
                    }
                    stop();
                }
                tick++;
            }
        });

    }

}
