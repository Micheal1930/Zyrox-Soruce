package com.varrock.world.content.minigames.impl.castlewars.object;

import com.varrock.model.Animation;
import com.varrock.model.GameObject;
import com.varrock.model.Position;
import com.varrock.model.definitions.WeaponAnimations;
import com.varrock.util.Misc;
import com.varrock.world.clip.region.RegionClipping;
import com.varrock.world.content.BonusManager;
import com.varrock.world.content.CustomObjects;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.minigames.impl.castlewars.CastleWarsManager;
import com.varrock.world.content.minigames.impl.castlewars.team.CastleWarsTeam;
import com.varrock.world.entity.impl.player.Player;

/**
 * Handles the Castle Wars double doors
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class CastleWarsDoubleDoor {

	/**
	 * The saradomin closed left door
	 */
	private static final GameObject SARADOMIN_CLOSED_LEFT_DOOR = new GameObject(4423, new Position(2426, 3088, 0), 0,
			3);

	/**
	 * The saradomin closed right door
	 */
	private static final GameObject SARADOMIN_CLOSED_RIGHT_DOOR = new GameObject(4424, new Position(2427, 3088, 0), 0,
			3);

	/**
	 * The saradomin closed left door
	 */
	private static final GameObject SARADOMIN_OPEN_LEFT_DOOR = new GameObject(4425, new Position(2426, 3088, 0), 0, 0);

	/**
	 * The saradomin closed right door
	 */
	private static final GameObject SARADOMIN_OPEN_RIGHT_DOOR = new GameObject(4426, new Position(2427, 3088, 0), 0, 2);

	/**
	 * The zamorak closed left door
	 */
	private static final GameObject ZAMORAK_CLOSED_LEFT_DOOR = new GameObject(4427, new Position(2373, 3119, 0), 0, 1);

	/**
	 * The zamorak closed right door
	 */
	private static final GameObject ZAMORAK_CLOSED_RIGHT_DOOR = new GameObject(4428, new Position(2372, 3119, 0), 0, 1);

	/**
	 * The zamorak opened left door
	 */
	private static final GameObject ZAMORAK_OPEN_LEFT_DOOR = new GameObject(4430, new Position(2373, 3119, 0), 0, 2);

	/**
	 * The zamorak open right door
	 */
	private static final GameObject ZAMORAK_OPEN_RIGHT_DOOR = new GameObject(4429, new Position(2372, 3119, 0), 0, 4);

	/**
	 * Attacking the door
	 * 
	 * @param player the player
	 */
	private static void attack(Player player) {
		/*
		 * The opposite team
		 */
		CastleWarsTeam team = CastleWarsManager.getOppositeTeam(player);
		/*
		 * The health of the gate
		 */
		if (team.getHealth() < 1) {
			return;
		}
		/*
		 * Check combat type
		 */
		if (player.getCombatBuilder().getStrategy() != null) {
			if (!player.getCombatBuilder().getStrategy().getCombatType().equals(CombatType.MELEE)) {
				player.getPacketSender().sendMessage("You can only use melee to attack the door!");
				return;
			}
		}
		/*
		 * Attack
		 */
		player.performAnimation(new Animation(WeaponAnimations.getAttackAnimation(player)));
		/*
		 * The damage
		 */
		int damage = (int) (player.getBonusManager().getOtherBonus()[BonusManager.BONUS_STRENGTH] / 10);
		/*
		 * Decrease health
		 */
		team.setHealth(team.getHealth() - (3 + Misc.random(damage)));
		/*
		 * Debug
		 */
		player.getPacketSender().debug("damage: " + damage + ", health: " + team.getHealth(),
				CastleWarsManager.TESTING);
		/*
		 * Remove
		 */
		if (team.getHealth() < 1) {
			if (CastleWarsManager.isSaradomin(player)) {
				openZamorakDoor();
			} else {
				openSaradominDoor();
			}
		}
	}

	/**
	 * Handles interaction with doors
	 * 
	 * @param player the player
	 * @param id     the door id
	 * @param x      the door x
	 * @param y      the door y
	 * @param type   the type
	 * @return doors
	 */
	public static boolean handleDoor(Player player, int id, int x, int y, int type) {
		if (type == 2) {
			switch (id) {
			case 4423:
			case 4424:
				if (CastleWarsManager.isSaradomin(player)) {
					player.getPacketSender().sendMessage("You can't attack your own teams door.");
					return true;
				}
				attack(player);
				return true;

			case 4427:
			case 4428:
				if (CastleWarsManager.isZamorak(player)) {
					player.getPacketSender().sendMessage("You can't attack your own teams door.");
					return true;
				}
				attack(player);
				return true;
			}
			return true;
		}
		switch (id) {
		case 4423:
		case 4424:
			if (CastleWarsManager.isSaradomin(player)) {
				openSaradominDoor();
			} else {
				player.getPacketSender().sendMessage("You must break the door down by attacking it to open it.");
			}
			return true;

		case 4425:
		case 4426:
			if (CastleWarsManager.saradomin.getHealth() > 0) {
				closeSaradominDoor();
			} else {
				player.getPacketSender().sendMessage("The door is broken and unable to be closed!");
			}
			return true;

		case 4427:
		case 4428:
			if (CastleWarsManager.isZamorak(player)) {
				openZamorakDoor();
			} else {
				player.getPacketSender().sendMessage("You must break the door down by attacking it to open it.");
			}
			return true;

		case 4429:
		case 4430:
			if (CastleWarsManager.zamorak.getHealth() > 0) {
				closeZamorakDoor();
			} else {
				player.getPacketSender().sendMessage("The door is broken and unable to be closed!");
			}
			return true;
		}
		return false;
	}

	/**
	 * Prepare the doors
	 */
	public static void prepare() {
		closeSaradominDoor();
	}

	/**
	 * Closing saradomin door
	 */
	private static void closeSaradominDoor() {
		CustomObjects.spawnGlobalObject(SARADOMIN_CLOSED_LEFT_DOOR);
		CustomObjects.spawnGlobalObject(SARADOMIN_CLOSED_RIGHT_DOOR);
	}

	/**
	 * Closing saradomin door
	 */
	private static void openSaradominDoor() {
		CustomObjects.spawnGlobalObject(SARADOMIN_OPEN_LEFT_DOOR);
		CustomObjects.spawnGlobalObject(SARADOMIN_OPEN_RIGHT_DOOR);
		RegionClipping.removeClipping(2427, 3087, 0, 0x000000);
		RegionClipping.removeClipping(2426, 3087, 0, 0x000000);
		RegionClipping.removeClipping(2427, 3088, 0, 0x000000);
		RegionClipping.removeClipping(2426, 3088, 0, 0x000000);
	}

	/**
	 * Closing zamorak door
	 */
	private static void closeZamorakDoor() {
		CustomObjects.spawnGlobalObject(ZAMORAK_CLOSED_LEFT_DOOR);
		CustomObjects.spawnGlobalObject(ZAMORAK_CLOSED_RIGHT_DOOR);
	}

	/**
	 * Closing zamorak door
	 */
	private static void openZamorakDoor() {
		CustomObjects.spawnGlobalObject(ZAMORAK_OPEN_LEFT_DOOR);
		CustomObjects.spawnGlobalObject(ZAMORAK_OPEN_RIGHT_DOOR);
		RegionClipping.removeClipping(2373, 3119, 0, 0x000000);
		RegionClipping.removeClipping(2372, 3119, 0, 0x000000);
		RegionClipping.removeClipping(2372, 3119, 0, 0x000000);
		RegionClipping.removeClipping(2373, 3120, 0, 0x000000);
	}
}