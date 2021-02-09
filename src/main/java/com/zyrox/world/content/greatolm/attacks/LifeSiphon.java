package com.zyrox.world.content.greatolm.attacks;

import com.zyrox.GameSettings;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.*;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.greatolm.OlmAnimations;
import com.zyrox.world.content.greatolm.RaidsParty;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

public class LifeSiphon {

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
                for (int i = 0; i < 2; i++) {
                    if (tick == 1) {
                        Position pos = Attacks.randomLocation(height);
                        NPC spawn = NPC.of(5090, pos);
                        World.register(spawn);
                        new Projectile(party.getGreatOlmNpc(), spawn, Attacks.BLUE_SMALL_PROJECTILE, 60, 8, 70, 10, 0)
                                .sendProjectile();
                        party.getLifeSiphonPositions().add(pos);
                        party.getOwner().getPacketSender().sendGlobalGraphic(new Graphic(1363 + GameSettings.OSRS_GFX_OFFSET, GraphicHeight.LOW),
                                pos);

                        TaskManager.submit(new Task(10) {
                            @Override
                            public void execute() {
                                World.deregister(spawn);
                                stop();
                            }
                        });

                    }
                }
                if (tick == 10) {
                    for (Player member : party.getPlayers()) {
                        if (member != null && member.isInsideRaids()) {
                            if (!member.getPosition().sameAs(party.getLifeSiphonPositions().get(0))
                                    && !member.getPosition().sameAs(party.getLifeSiphonPositions().get(1))) {
                                int hit = Misc.random(100, 300);
                                member.dealDamage(new Hit(hit, Hitmask.RED, CombatIcon.NONE));
                                party.getGreatOlmNpc().heal(hit * 4);
                            }
                        }
                    }
                }
                if (tick == 12) {
                    party.getLifeSiphonPositions().clear();
                    stop();
                }
                if (tick == 2) {
                    OlmAnimations.resetAnimation(party);
                }

                tick++;
            }
        });

    }

}
