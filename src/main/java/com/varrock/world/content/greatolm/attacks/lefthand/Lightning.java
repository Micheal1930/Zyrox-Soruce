package com.varrock.world.content.greatolm.attacks.lefthand;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.*;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.prayer.CurseHandler;
import com.varrock.world.content.combat.prayer.PrayerHandler;
import com.varrock.world.content.greatolm.OlmAnimations;
import com.varrock.world.content.greatolm.RaidsParty;
import com.varrock.world.content.greatolm.attacks.Attacks;
import com.varrock.world.entity.impl.player.Player;

public class Lightning {

    public static void performAttack(RaidsParty party, int height) {
        party.setLeftHandAttackTimer(20);

        TaskManager.submit(new Task(1, party, true) {
            int tick = 0;

            @Override
            public void execute() {
                if (party.isLeftHandDead()) {
                    stop();
                }
                if (tick == 1) {
                    party.getLeftHandObject().performAnimation(OlmAnimations.flashingLightningLeftHand);
                }
                if (tick == 3) {
                    for (int i = 0; i < 2; i++) {
                        party.getLightningSpots()[i] = new Position(Attacks.randomLocation(height).getX(), 5748, height);
                        party.getOwner().getPacketSender().sendGlobalGraphic(new Graphic(Attacks.GREEN_LIGHTNING), party.getLightningSpots()[i]);
                    }
                    for (int i = 2; i < 4; i++) {
                        party.getLightningSpots()[i] = new Position(Attacks.randomLocation(height).getX(), 5731, height);
                        party.getOwner().getPacketSender().sendGlobalGraphic(new Graphic(Attacks.GREEN_LIGHTNING), party.getLightningSpots()[i]);
                    }
                }

                if (tick >= 4) {
                    for (int i = 0; i < 2; i++) {
                        party.getLightningSpots()[i] = new Position(party.getLightningSpots()[i].getX(), party.getLightningSpots()[i].getY() - 1, height);
                    }
                    for (int i = 2; i < 4; i++) {
                        party.getLightningSpots()[i] = new Position(party.getLightningSpots()[i].getX(), party.getLightningSpots()[i].getY() + 1, height);
                    }
                    for (int i = 0; i < 4; i++) {
                        party.getOwner().getPacketSender().sendGlobalGraphic(new Graphic(Attacks.GREEN_LIGHTNING), party.getLightningSpots()[i]);
                        if (party.getLightningSpots()[i].getY() > 5748 || party.getLightningSpots()[i].getY() < 5731) {
                            stop();
                        }
                        for (Player member : party.getPlayers()) {
                            if (member != null && member.isInsideRaids()) {
                                if (member.getPosition().sameAs(party.getLightningSpots()[i])) {
                                    member.getMovementQueue().freeze(2);
                                    PrayerHandler.deactivateAll(member);
                                    CurseHandler.deactivateAll(member);
                                    member.sendMessage("@red@You've been electocuted to the spot!");
                                    member.sendMessage("You've been injured and can't use protection prayers!");
                                    Attacks.hitPlayer(member, 100, 250, CombatType.MAGIC, CombatIcon.NONE, 2, false);
                                }
                            }
                        }
                    }
                }

                if (tick == 20) {
                    stop();
                }
                tick++;
            }
        });
    }

}
