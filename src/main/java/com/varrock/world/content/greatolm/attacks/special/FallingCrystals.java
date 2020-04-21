package com.varrock.world.content.greatolm.attacks.special;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.*;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.greatolm.OlmAnimations;
import com.varrock.world.content.greatolm.RaidsParty;
import com.varrock.world.content.greatolm.attacks.Attacks;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

public class FallingCrystals {

    public static void performAttack(RaidsParty party, int height) {
        party.getGreatOlmNpc().performGreatOlmAttack(party);
        party.setOlmAttackTimer(6);

        party.sendMessage("The Great Olm sounds a cry...");
        TaskManager.submit(new Task(1, party, true) {
            int tick = 0;

            @Override
            public void execute() {
                if (party.getGreatOlmNpc().isDying() || party.isSwitchingPhases()) {
                    stop();
                }
                if (tick == 1) {
                    int random = Misc.getRandom(party.getPlayersInRaidsDungeon(party) - 1);
                    party.setFallingCrystalsPlayer(party.getPlayersInRaids().get(random));
                    party.getFallingCrystalsPlayer()
                            .sendMessage("@red@The Great Olm has chosen you as its target - watch out!");
                }
                if (tick == 2) {
                    OlmAnimations.resetAnimation(party);
                }
                for (int iz = 0; iz < 23; iz++) {
                    if (tick == 23) {
                        party.getDripPools().clear();
                        stop();
                    }
                    if (tick == (2 * iz) + 3) {
                        if (party.getFallingCrystalsPlayer().isInsideRaids())
                            party.getFallingCrystalsPlayer()
                                    .setGraphic(new Graphic(Attacks.RED_CIRCLE, GraphicHeight.LOW));
                    }
                    if (tick == (2 * iz) + 2) {
                        if (party.getFallingCrystalsPlayer() != null) {
                            if (party.getFallingCrystalsPlayer().isInsideRaids()) {
                                Position pos = party.getFallingCrystalsPlayer().getPosition();
                                NPC spawn = NPC.of(5090, pos);
                                World.register(spawn);
                                NPC spawn1 = NPC.of(5090, new Position(pos.getX(), pos.getY() - 1, height));
                                World.register(spawn1);
                                new Projectile(spawn1, spawn, Attacks.CRYSTAL, 55, 1, 240, 0, 0).sendProjectile();

                                TaskManager.submit(new Task(1) {
                                    @Override
                                    public void execute() {
                                        party.getOwner().getPacketSender().sendGlobalGraphic(
                                                new Graphic(Attacks.LEFTOVER_CRYSTALS, GraphicHeight.MIDDLE), pos);
                                        for (Player member : party.getPlayers()) {
                                            if (member != null && member.isInsideRaids()) {
                                                if (member.getPosition().sameAs(pos)) {
                                                    member.dealDamage(new Hit(
                                                            Misc.random(100, 200), Hitmask.RED, CombatIcon.NONE));
                                                    member.sendMessage("The falling crystal shatters into you.");
                                                }
                                            }
                                        }

                                        World.deregister(spawn);
                                        World.deregister(spawn1);
                                        stop();
                                    }
                                });
                            }
                        }

                    }
                }

                tick++;
            }
        });

    }
}
