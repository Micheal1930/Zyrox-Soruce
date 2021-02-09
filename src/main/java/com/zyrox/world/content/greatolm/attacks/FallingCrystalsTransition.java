package com.zyrox.world.content.greatolm.attacks;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.*;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.greatolm.GreatOlm;
import com.zyrox.world.content.greatolm.RaidsParty;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

public class FallingCrystalsTransition {

    public static void performAttack(RaidsParty party, int height, int tick) {

        boolean doAction = false;
        boolean doActionPlayer = false;

        for (int i = 0; i < 10000; i++) {
            if (tick == i * 2)
                doAction = true;
            if (tick == i * 3)
                doActionPlayer = true;
        }
        if (party.isTransitionPhase()
                || (party.getCurrentPhase() == 3 && party.isLeftHandDead() && party.isRightHandDead()
                && party.getGreatOlmNpc().getConstitution() > 0 && !party.getGreatOlmNpc().isDying())) {
            if (doActionPlayer && Misc.getRandom(1) == 1) {
                if (party.getPlayersInRaids().size() >= 1) {
                    Player member = party.getPlayers().get(Misc.getRandom(party.getPlayersInRaids().size() - 1));

                    if (member.isInsideRaids() && GreatOlm.insideChamber(member)) {
                        Position posPlayer = member.getPosition();
                        NPC spawn = NPC.of(5090, posPlayer);
                        World.register(spawn);
                        NPC spawn1 = NPC.of(5090, new Position(posPlayer.getX(), posPlayer.getY() - 1, height));
                        World.register(spawn1);
                        new Projectile(spawn1, spawn, Attacks.FALLING_CRYSTAL, 140, 1, 220, 0, 0).sendProjectile();

                        TaskManager.submit(new Task(4) {
                            @Override
                            public void execute() {
                                party.getOwner().getPacketSender()
                                        .sendGlobalGraphic(new Graphic(Attacks.GREEN_PUFF), posPlayer);

                                for (Player member : party.getPlayers()) {
                                    if (member != null && member.isInsideRaids()) {
                                        if (member.getPosition().sameAs(posPlayer)) {
                                            member.dealDamage(
                                                    new Hit(Misc.random(300, 450), Hitmask.RED, CombatIcon.NONE));
                                        } else if (Locations.goodDistance(member.getPosition(), posPlayer, 1)) {
                                            member.dealDamage(
                                                    new Hit(Misc.random(100, 300), Hitmask.RED, CombatIcon.NONE));
                                        }
                                    }
                                }

                                World.deregister(spawn);
                                World.deregister(spawn1);
                                stop();
                            }
                        });
                    }
                } else {
                    dropCrystal(party, height);
                }
            }
            if (doAction) {
                dropCrystal(party, height);
            }
            for (Player member : party.getPlayers()) {
                if (member != null && member.isInsideRaids()) {
                    member.getPacketSender().sendCameraShake(2, 2, 1, 1);
                }
            }
        } else {
            for (Player member : party.getPlayers()) {
                if (member != null && member.isInsideRaids()) {
                    member.getPacketSender().sendCameraNeutrality();
                }
            }

        }
    }

    public static void dropCrystal(RaidsParty party, int height) {

        Position pos = Attacks.randomLocation(height);
        NPC spawn = NPC.of(5090, pos);
        World.register(spawn);
        NPC spawn1 = NPC.of(5090, new Position(pos.getX(), pos.getY() - 1, height));
        World.register(spawn1);
        new Projectile(spawn1, spawn, Attacks.FALLING_CRYSTAL, 140, 1, 220, 0, 0).sendProjectile();

        TaskManager.submit(new Task(4) {

            @Override
            public void execute() {
                party.getOwner().getPacketSender().sendGlobalGraphic(new Graphic(Attacks.GREEN_PUFF), pos);

                for (Player member : party.getPlayers()) {
                    if (member != null && member.isInsideRaids()) {
                        if (member.getPosition().sameAs(pos)) {
                            member.dealDamage(new Hit(Misc.random(300, 450), Hitmask.RED, CombatIcon.NONE));
                        } else if (Locations.goodDistance(member.getPosition(), pos, 1)) {
                            member.dealDamage(new Hit(Misc.random(100, 300), Hitmask.RED, CombatIcon.NONE));
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
