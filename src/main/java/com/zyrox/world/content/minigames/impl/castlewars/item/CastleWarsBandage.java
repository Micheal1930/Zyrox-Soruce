package com.zyrox.world.content.minigames.impl.castlewars.item;

import com.zyrox.model.Skill;
import com.zyrox.model.Locations.Location;
import com.zyrox.world.content.minigames.impl.castlewars.CastleWarsManager;
import com.zyrox.world.content.minigames.impl.castlewars.object.CastleWarsSingleDoor;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Represents a castle wars bandage
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class CastleWarsBandage {

	/**
	 * Healing using the bandage
	 * 
	 * @param player the player
	 */
	public static void heal(Player player) {
		/*
		 * Not in castle wars
		 */
		if (!player.getLocation().equals(Location.CASTLE_WARS_GAME)) {
			return;
		}
		/*
		 * The amount to heal
		 */
		int heal = player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) / 10;
		/*
		 * Restore
		 */
		player.heal(heal);
		player.setPoisonDamage(0);
		player.setRunEnergy(100);
	}

	/**
	 * Using the bandage on a team mate
	 * 
	 * @param player      the player
	 * @param otherPlayer the other player
	 */
	public static void useOnPlayer(Player player, Player otherPlayer) {
		/*
		 * Not in castle wars
		 */
		if (!player.getLocation().equals(Location.CASTLE_WARS_GAME)) {
			return;
		}
		/*
		 * Not in castle wars
		 */
		if (!otherPlayer.getLocation().equals(Location.CASTLE_WARS_GAME)) {
			return;
		}
		/*
		 * Not on the same team
		 */
		if(!CastleWarsManager.sameTeam(player, otherPlayer)) {
			return;
		}
		/*
		 * Delete
		 */
		player.getInventory().delete(CastleWarsManager.BANDAGE);
		player.setPositionToFace(otherPlayer.getPosition().copy());
		player.performAnimation(CastleWarsSingleDoor.PICK_LOCKET_DOOR);
		/*
		 * Heal
		 */
		heal(otherPlayer);
	}
}
