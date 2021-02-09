package com.zyrox.net.packet.impl;

import com.zyrox.model.Animation;
import com.zyrox.model.Position;
import com.zyrox.model.RegionInstance.RegionInstanceType;
import com.zyrox.model.movement.PathFinder;
import com.zyrox.net.packet.Packet;
import com.zyrox.net.packet.PacketListener;
import com.zyrox.world.content.AFKActivityThrottler;
import com.zyrox.world.content.BankPin;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.minigames.impl.Dueling;
import com.zyrox.world.content.minigames.impl.Dueling.DuelRule;
import com.zyrox.world.content.tutorial.TutorialStages;
import com.zyrox.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player has clicked on
 * either the mini-map or the actual game map to move around.
 *
 * @author Gabriel Hannason
 */

public class MovementPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {

        int size = packet.getSize();

        if (packet.getOpcode() == 248)
            size -= 14;

        int steps = (size - 5) / 2;

        if (steps < 0) {
            return;
        }

        int firstStepX = packet.readLEShortA();

        int[][] path = new int[steps][2];

        for (int i = 0; i < steps; i++) {
            path[i][0] = packet.readByte();
            path[i][1] = packet.readByte();
        }

        int firstStepY = packet.readLEShort();

        for (int i = 0; i < steps; i++) {
            path[i][0] += firstStepX;
            path[i][1] += firstStepY;
        }

        Position[] positions = new Position[steps + 1];
        positions[0] = new Position(firstStepX, firstStepY, player.getPosition().getZ());
        for (int i = 0; i < steps; i++) {
            positions[i + 1] = new Position(path[i][0], path[i][1], player.getPosition().getZ());
        }

        if (player.getTutorialStage() != TutorialStages.COMPLETED) {
            DialogueManager.start(player, player.getTutorialStage().getDialogue());
            return;
        }

        final Position endDestination = new Position(positions[steps].getX(), positions[steps].getY(), player.getPosition().getZ());

        if(player.isQuickMovement()) {
            player.moveTo(endDestination);
            return;
        }


        player.setEntityInteraction(null);
        player.getSkillManager().stopSkilling();

        if (packet.getOpcode() != COMMAND_MOVEMENT_OPCODE) {
            if (player.getSkill() != null) {
                player.setSkill(null);
            }
            if (player.getHarvestSkill() != null) {
                player.getHarvestSkill().stopHarvest();
            }
            /*if (player.getProductionSkill() != null) {
                player.getProductionSkill().stopHarvest();
            }*/
        }

        player.getMovementQueue().setFollowCharacter(null);

        if (packet.getOpcode() != COMMAND_MOVEMENT_OPCODE) {
            player.setWalkToTask(null);
            player.setCastSpell(null);
            player.getCombatBuilder().cooldown(false);
        }
        AFKActivityThrottler.unregisterAfk(player);

        if (!checkReqs(player, packet.getOpcode()))
            return;

        //player.getLogging().log("Movement: " + player.getPosition().toString());
        player.getPacketSender().sendInterfaceRemoval();
        player.setTeleporting(false);
        player.setInactive(false);

        if (player.getRegionInstance() != null && player.getRegionInstance().getType() == RegionInstanceType.CONSTRUCTION_HOUSE) {
            Position first = new Position(firstStepX, firstStepY, player.getPosition().getZ());

            boolean invalidStep = false;

            if (!player.getPosition().isViewableFrom(first)) {
                invalidStep = true;
            } else {
                for (int i = 1; i < path.length; i++) {
                    int distance = player.getPosition().getDistance(new Position(path[i][0], path[i][1], player.getPosition().getZ()));

                    if (distance < -22 || distance > 22) {
                        invalidStep = true;
                        break;
                    }
                }
            }

            if (invalidStep) {
                player.getMovementQueue().reset();
                return;
            }

            if (player.getMovementQueue().addFirstStep(first)) {
                for (int i = 0; i < path.length; i++) {
                    player.getMovementQueue().addStep(new Position(path[i][0], path[i][1], player.getPosition().getZ()));
                }
            }
        } else {
            if (steps > 0) {
                if ((Math.abs(path[(steps - 1)][0] - player.getPosition().getX()) > 21) || (Math.abs(path[(steps - 1)][1] - player.getPosition().getY()) > 21)) {
                    player.getMovementQueue().reset();
                }
            } else if ((Math.abs(firstStepX - player.getPosition().getX()) > 21) || (Math.abs(firstStepY - player.getPosition().getY()) > 21)) {
                player.getMovementQueue().reset();
                return;
            }

            if (steps > 0)
                PathFinder.findPath(player, path[(steps - 1)][0], path[(steps - 1)][1], true, 16, 16);
            else {
                PathFinder.findPath(player, firstStepX, firstStepY, true, 16, 16);
            }
        }
    }

    public static boolean checkReqs(Player player, int opcode) {
        if (player.isFrozen()) {
            if (opcode != COMMAND_MOVEMENT_OPCODE)
                player.getPacketSender().sendMessage("A magical spell has made you unable to move.");
            return false;
        }
        if (player.getTrading().inTrade() && System.currentTimeMillis() - player.getTrading().lastAction <= 1000) {
            return false;
        }
        if (Dueling.checkRule(player, DuelRule.NO_MOVEMENT)) {
            if (opcode != COMMAND_MOVEMENT_OPCODE)
                player.getPacketSender().sendMessage("Movement has been turned off in this duel!");
            return false;
        }
        if (player.isResting()) {
            player.setResting(false);
            player.performAnimation(new Animation(11788));
            return false;
        }
        if (player.isAccountCompromised()) {
            player.sendMessage("@red@We have a reason to believe that this account was compromised.");
            player.sendMessage("@red@Please contact a staff member to recover this account.");
            return false;
        }
        if (player.requiresUnlocking()) {
            BankPin.init(player, false);
            return false;
        }
        if (player.isPlayerLocked() || player.isCrossingObstacle())
            return false;
        if (player.isNeedsPlacement()) {
            return false;
        }
        return !player.getMovementQueue().isLockMovement();
    }

    public static final int COMMAND_MOVEMENT_OPCODE = 98;
    public static final int GAME_MOVEMENT_OPCODE = 164;
    public static final int MINIMAP_MOVEMENT_OPCODE = 248;

}