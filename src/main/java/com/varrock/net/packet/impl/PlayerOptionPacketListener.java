
package com.varrock.net.packet.impl;

import com.varrock.model.PlayerInteractingOption;
import com.varrock.model.Locations.Location;
import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.world.World;
import com.varrock.world.content.combat.CombatFactory;
import com.varrock.world.content.tutorial.TutorialStages;
import com.varrock.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player has clicked on another player's
 * menu actions.
 * 
 * @author relex lawl
 */

public class PlayerOptionPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0) {
			return;
		}
		
		if (player.isTeleporting()) {
			return;
		}
		
		if (player.isSpectatorMode()) {
			player.sendMessage("You cannot do this in spectator mode.");
			return;
		}

		if (player.isAccountCompromised() || player.requiresUnlocking()) {
			return;
		}

		switch(packet.getOpcode()) {
		case 153:
			attack(player, packet);
			break;
		case 128:
			option1(player, packet);
			break;
		case 37:
			option2(player, packet);
			break;
		case 227:
			option3(player, packet);
			break;
		}
	}

	private static void attack(Player player, Packet packet) {
		int index = packet.readLEShort();
		if(index > World.getPlayers().capacity() || index < 0)
			return;
		final Player attacked = World.getPlayers().get(index);

		if(player.getTutorialStage() != TutorialStages.COMPLETED) {
			return;
		}

		if(attacked == null || attacked.getConstitution() <= 0 || attacked.equals(player)) {
			player.getMovementQueue().reset();
			return;
		}

		if(player.getLocation() == Location.RAIDS && player.getPlayerInteractingOption() == PlayerInteractingOption.INVITE) {
			if(player.getMinigameAttributes().getRaidsAttributes().getParty() == null) {
				player.getPacketSender().sendMessage("You must create a party before inviting players.");
				return;
			}
			if(player.getMinigameAttributes().getRaidsAttributes().getParty().getPlayers().contains(attacked)) {
				player.getPacketSender().sendMessage("That player is already in your party.");
				return;
			}
			player.sendMessage("Sent invite to " + attacked.getName());
			player.getMinigameAttributes().getRaidsAttributes().getParty().invite(attacked);
			return;
		} else if(player.getRaids().getTheatreOfBlood().inEntrance()) {
			 player.getRaids().getTheatreOfBlood().attemptInvite(attacked);
		}

		if(player.getLocation() == Location.DUEL_ARENA && player.getDueling().duelingStatus == 0) {
			player.getDueling().challengePlayer(attacked);
			return;
		}
		
		if(player.getCombatBuilder().getStrategy() == null) {
			player.getCombatBuilder().determineStrategy();
		}
		if (CombatFactory.checkAttackDistance(player, attacked)) {
			player.getMovementQueue().reset();
		}
		
		player.getCombatBuilder().attack(attacked);
	}

	/**
	 * Manages the first option click on a player option menu.
	 * @param player	The player clicking the other entity.
	 * @param packet	The packet to read values from.
	 */
	private static void option1(Player player, Packet packet) {
		int id = packet.readShort() & 0xFFFF;
		if(id < 0 || id > World.getPlayers().capacity())
			return;
		Player victim = World.getPlayers().get(id);
		if (victim == null)
			return;

		if(player.getTutorialStage() != TutorialStages.COMPLETED) {
			return;
		}
		/*GameServer.getTaskScheduler().schedule(new WalkToTask(player, victim.getPosition(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				//do first option here
			}
		}));*/
	}

	/**
	 * Manages the second option click on a player option menu.
	 * @param player	The player clicking the other entity.
	 * @param packet	The packet to read values from.
	 */
	private static void option2(Player player, Packet packet) {
		int id = packet.readShort() & 0xFFFF;
		if(id < 0 || id > World.getPlayers().capacity())
			return;
		Player victim = World.getPlayers().get(id);
		if (victim == null)
			return;

		if(player.getTutorialStage() != TutorialStages.COMPLETED) {
			return;
		}
		/*GameServer.getTaskScheduler().schedule(new WalkToTask(player, victim.getPosition(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				//do second option here
			}
		}));*/
	}

	/**
	 * Manages the third option click on a player option menu.
	 * @param player	The player clicking the other entity.
	 * @param packet	The packet to read values from.
	 */
	private static void option3(Player player, Packet packet) {
		int id = packet.readLEShortA() & 0xFFFF;
		if(id < 0 || id > World.getPlayers().capacity())
			return;
		Player victim = World.getPlayers().get(id);
		if (victim == null)
			return;

		if(player.getTutorialStage() != TutorialStages.COMPLETED) {
			return;
		}
		/*GameServer.getTaskScheduler().schedule(new WalkToTask(player, victim.getPosition(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				//do third option here
			}
		}));*/
	}
}
