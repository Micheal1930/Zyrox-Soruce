package com.varrock.world.content.greatolm.attacks.special;

import com.varrock.GameSettings;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.*;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.CustomObjects;
import com.varrock.world.content.greatolm.OlmAnimations;
import com.varrock.world.content.greatolm.RaidsParty;
import com.varrock.world.content.greatolm.attacks.Attacks;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

public class CrystalBombs {

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
                        GameObject
                                bomb = new GameObject(129766, pos);
                        NPC spawn = NPC.of(5090, pos);
                        World.register(spawn);
                        new Projectile(party.getGreatOlmNpc(), spawn, Attacks.FALLING_CRYSTAL, 60, 8, 70, 10, 0)
                                .sendProjectile();
                        TaskManager.submit(new Task(1) {
                            @Override
                            public void execute() {
                                CustomObjects.spawnTempObject(bomb, party.getOwner(), 6);
                                stop();
                            }
                        });

                        TaskManager.submit(new Task(7) {
                            @Override
                            public void execute() {
                                party.getOwner().getPacketSender()
                                        .sendGlobalGraphic(new Graphic(40  + GameSettings.OSRS_GFX_OFFSET, GraphicHeight.LOW), pos);

                                for (Player member : party.getPlayers()) {
                                    if (member != null && member.isInsideRaids()) {
                                        if (member.getPosition().sameAs(pos)) {
                                            member.dealDamage(
                                                    new Hit(Misc.random(500, 700), Hitmask.RED, CombatIcon.NONE));
                                        } else if (Locations.goodDistance(member.getPosition(), pos, 1)) {
                                            member.dealDamage(
                                                    new Hit(Misc.random(300, 500), Hitmask.RED, CombatIcon.NONE));
                                        } else if (Locations.goodDistance(member.getPosition(), pos, 2)) {
                                            member.dealDamage(
                                                    new Hit(Misc.random(150, 300), Hitmask.RED, CombatIcon.NONE));
                                        }
                                    }
                                }
                                World.deregister(spawn);

                                stop();
                            }
                        });

                    }
                }
                if (tick == 10) {
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
