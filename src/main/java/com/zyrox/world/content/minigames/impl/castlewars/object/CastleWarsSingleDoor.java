package com.zyrox.world.content.minigames.impl.castlewars.object;

import com.zyrox.model.Animation;
import com.zyrox.model.GameObject;
import com.zyrox.model.Position;
import com.zyrox.util.Misc;
import com.zyrox.world.content.CustomObjects;
import com.zyrox.world.content.minigames.impl.castlewars.CastleWarsManager;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Handles the Castle Wars single doors
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class CastleWarsSingleDoor {

	/**
	 * The open zamorak door
	 */
	private static final GameObject OPEN_ZAMORAK_DOOR = new GameObject(4468, new Position(2384, 3134, 0), 0, 3);

	/**
	 * The close zamorak door
	 */
	public static final GameObject CLOSE_ZAMORAK_DOOR = new GameObject(4467, new Position(2385, 3134, 0), 0, 4);

	/**
	 * The open saradomin door
	 */
	private static final GameObject OPEN_SARADOMIN_DOOR = new GameObject(4466, new Position(2414, 3073, 0), 0, 1);

	/**
	 * The close saradomin door
	 */
	public static final GameObject CLOSE_SARADOMIN_DOOR = new GameObject(4465, new Position(2415, 3073, 0), 0, 4);

	/**
	 * The pick locket door animation
	 */
	public static final Animation PICK_LOCKET_DOOR = new Animation(0x340);

	/**
	 * The chance of opening the door
	 */
	private static final int DOOR_OPEN_CHANCE = 3;

	/**
	 * Handles interaction with doors
	 * 
	 * @param player the player
	 * @param id     the door id
	 * @param x      the door x
	 * @param y      the door y
	 * @return doors
	 */
	public static boolean handleDoor(Player player, int id, int x, int y) {
		switch (id) {
		case 4468:
			closeZamorakDoor();
			return true;
		case 4467:
			if (CastleWarsManager.isZamorak(player)) {
				openZamorakDoor();
			} else {
				if (Misc.random(DOOR_OPEN_CHANCE) == 1) {
					player.performAnimation(PICK_LOCKET_DOOR);
					openZamorakDoor();
					player.getPacketSender().sendMessage("You pick the lock on the door.");
				}
			}
			return true;
		case 4465:
			if (CastleWarsManager.isSaradomin(player)) {
				openSaradominDoor();
			} else {
				if (Misc.random(DOOR_OPEN_CHANCE) == 1) {
					player.performAnimation(PICK_LOCKET_DOOR);
					openSaradominDoor();
					player.getPacketSender().sendMessage("You pick the lock on the door.");
				}
			}
			return true;
		case 4466:
			closeSaradominDoor();
			return true;
		}
		return false;
	}

	/**
	 * Closing all door
	 */
	public static void prepare() {
		closeZamorakDoor();
		closeSaradominDoor();
	}

	/**
	 * Closing zamorak door
	 */
	private static void closeZamorakDoor() {
		CustomObjects.deleteGlobalObject(OPEN_ZAMORAK_DOOR);
		CustomObjects.spawnGlobalObject(CLOSE_ZAMORAK_DOOR);
		CastleWarsManager.zamorak.setDoorLocked(true);
	}

	/**
	 * Opening zamorak door
	 */
	private static void openZamorakDoor() {
		CustomObjects.deleteGlobalObject(CLOSE_ZAMORAK_DOOR);
		CustomObjects.spawnGlobalObject(OPEN_ZAMORAK_DOOR);
		CastleWarsManager.zamorak.setDoorLocked(false);
	}

	/**
	 * Closing saradomin door
	 */
	private static void closeSaradominDoor() {
		CustomObjects.deleteGlobalObject(OPEN_SARADOMIN_DOOR);
		CustomObjects.spawnGlobalObject(CLOSE_SARADOMIN_DOOR);
		CastleWarsManager.saradomin.setDoorLocked(true);
	}

	/**
	 * Opening saradomin door
	 */
	private static void openSaradominDoor() {
		CustomObjects.deleteGlobalObject(CLOSE_SARADOMIN_DOOR);
		CustomObjects.spawnGlobalObject(new GameObject(4466, new Position(2415, 3073, 0), 0, 1));
		CastleWarsManager.saradomin.setDoorLocked(false);
	}
}