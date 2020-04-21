package com.varrock.world.content.minigames.impl.castlewars.item.flag;

import java.util.Objects;
import java.util.Optional;

import com.varrock.model.Flag;
import com.varrock.model.GameObject;
import com.varrock.model.Item;
import com.varrock.model.Position;
import com.varrock.model.container.impl.Equipment;
import com.varrock.util.Misc;
import com.varrock.world.content.CustomObjects;
import com.varrock.world.content.minigames.impl.castlewars.CastleWarsManager;
import com.varrock.world.content.minigames.impl.castlewars.team.CastleWarsTeam;
import com.varrock.world.entity.impl.player.Player;

/**
 * Handles the Castle Wars Flag
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class CastleWarsFlagManager {

	/**
	 * Capturing flag
	 * 
	 * @param player the player
	 * @param object the object
	 */
	public static void captureFlag(Player player, int id, int x, int y) {
		/*
		 * The flag
		 */
		Optional<CastleWarsFlag> flag = CastleWarsFlag.getCapture(id);
		/*
		 * No flag
		 */
		if (!flag.isPresent()) {
			return;
		}
		/*
		 * The team
		 */
		CastleWarsTeam team = CastleWarsManager.getTeam(player);
		/*
		 * Scoring
		 */
		if (score(player, id)) {
			return;
		}

		/*
		 * Owner flag
		 */
		if (team.getFlag().equals(flag.get())) {
			player.getPacketSender().sendMessage("The flag is where it should be.");
			return;
		}

		/*
		 * The other team
		 */
		CastleWarsTeam otherTeam = CastleWarsManager.getOppositeTeam(player);
		/*
		 * Flag is taken
		 */
		if (otherTeam.isFlagTaken()) {
			if (!CastleWarsManager.TESTING) {
				return;
			}
		}
		/*
		 * Has weapon
		 */
		if (!player.getEquipment().isEmpty(Equipment.WEAPON_SLOT)) {
			player.getPacketSender().sendMessage("You need to take your weapon off to pick up the flag.");
			return;
		}
		/*
		 * Set flag state
		 */
		otherTeam.setFlagTaken(true);
		otherTeam.setFlagDropped(false);
		/*
		 * Equip flag
		 */
		player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(flag.get().getId()));
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		player.getEquipment().refreshItems();
		/*
		 * Update flag
		 */
		updateFlag(otherTeam);
	}

	/**
	 * Dropping the flag
	 * 
	 * @param player the player
	 */
	public static boolean dropFlag(Player player) {
		/*
		 * The weapon
		 */
		Item id = player.getEquipment().get(Equipment.WEAPON_SLOT);
		/*
		 * No weapon
		 */
		if (id == null) {
			return true;
		}
		/*
		 * The flag
		 */
		Optional<CastleWarsFlag> flag = CastleWarsFlag.getForId(id.getId());
		/*
		 * No flag
		 */
		if (!flag.isPresent()) {
			return true;
		}
		/*
		 * Set object
		 */
		GameObject object = new GameObject(flag.get().getDropped(), player.getPosition().copy().add(0,
				player.getPosition().getZ() >= 2 ? -3 : player.getPosition().getZ() == 1 ? -1 : 0));
		if (CustomObjects.getGameObject(object.getPosition()) != null) {
			return false;
		}
		/*
		 * Delete flag
		 */
		player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(-1));
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		player.getEquipment().refreshItems();
		/*
		 * The opposite team
		 */
		CastleWarsTeam team = CastleWarsManager.getTeamForFlag(flag.get());
		/*
		 * Set flag state
		 */
		team.setFlagTaken(false);
		team.setFlagDropped(true);
		/*
		 * Debug
		 */
		player.getPacketSender().debug(
				team + ".[ drop flag ]: taken: " + team.isFlagTaken() + ", dropped: " + team.isFlagDropped(),
				CastleWarsManager.TESTING);
		player.getPacketSender().debug("add: " + object.getPosition(), CastleWarsManager.TESTING);
		CustomObjects.spawnGlobalObject(object);
		player.getEquipment().refreshItems();
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		return true;
	}

	/**
	 * Taking the flag
	 * 
	 * @param player the player
	 * @param id     the id
	 * @param x      the x
	 * @param y      the y
	 */
	public static void takeFlag(Player player, int id, int x, int y) {
		/*
		 * The flag
		 */
		Optional<CastleWarsFlag> flag = CastleWarsFlag.getDropped(id);
		/*
		 * No flag
		 */
		if (!flag.isPresent()) {
			return;
		}
		/*
		 * The flag team
		 */
		CastleWarsTeam flagTeam = CastleWarsManager.getTeamForFlag(flag.get());
		/*
		 * Not dropped
		 */
		if (!flagTeam.isFlagDropped()) {
			if (!CastleWarsManager.TESTING) {
				return;
			}
		}
		/*
		 * Has weapon
		 */
		if (!player.getEquipment().isEmpty(Equipment.WEAPON_SLOT)) {
			player.getPacketSender().sendMessage("You need to take your weapon off to pick up the flag.");
			return;
		}
		/*
		 * Set flag
		 */
		flagTeam.setFlagDropped(false);
		/*
		 * The existing flag
		 */
		GameObject existing = new GameObject(flag.get().getDropped(),
				new Position(x, y - (player.getPosition().getZ() == 3 ? 3 : player.getPosition().getZ() == 1 ? 1 : 0),
						player.getPosition().getZ()));
		player.getPacketSender().debug("delete: " + existing.getPosition(), CastleWarsManager.TESTING);
		CustomObjects.deleteGlobalObject(existing);
		/*
		 * Debug
		 */
		player.getPacketSender().debug(
				flagTeam + ". take flag taken: " + flagTeam.isFlagTaken() + ", dropped: " + flagTeam.isFlagDropped(),
				CastleWarsManager.TESTING);
		/*
		 * The flag team
		 */
		CastleWarsTeam team = CastleWarsManager.getTeam(player);
		/*
		 * Own flag
		 */
		if (team.getFlag().equals(flag.get())) {
			/*
			 * Inside castle
			 */
			if (team.getCastle().inside(player)) {
				/*
				 * Set flag state
				 */
				flagTeam.setFlagTaken(false);
				/*
				 * Set object
				 */
				GameObject object = new GameObject(flag.get().getCapture(), flag.get().getPosition());
				CustomObjects.spawnGlobalObject(object);
				return;
			}
		}
		/*
		 * Pickup flag
		 */
		player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(flag.get().getId()));
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		player.getEquipment().refreshItems();
		flagTeam.setFlagTaken(true);
	}

	/**
	 * Scoring
	 * 
	 * @param player the player
	 * @param id     the id
	 */
	public static boolean score(Player player, int id) {
		/*
		 * The standard
		 */
		Optional<CastleWarsFlag> flag = CastleWarsFlag.getForScore(id);
		/*
		 * No standard
		 */
		if (!flag.isPresent()) {
			return false;
		}
		/*
		 * Testing
		 */
		player.getPacketSender().debug(flag.get() + " flag", CastleWarsManager.TESTING);
		/*
		 * The team
		 */
		CastleWarsTeam team = CastleWarsManager.getTeam(player);
		/*
		 * Same team
		 */
		if (!team.getFlag().equals(flag.get())) {
			/*
			 * Testing
			 */
			player.getPacketSender().debug("team doesn't support standard. Not same team.", CastleWarsManager.TESTING);
			return false;
		}
		/*
		 * The opposite team
		 */
		CastleWarsTeam opposite = CastleWarsManager.getOppositeTeam(player);
		/*
		 * Flag taken
		 */
		if (!opposite.isFlagTaken() || opposite.isFlagDropped()) {
			/*
			 * Debug
			 */
			player.getPacketSender().debug(
					team + " not okay. taken: " + team.isFlagTaken() + ", dropped:" + team.isFlagDropped(),
					CastleWarsManager.TESTING);
			return false;
		}
		/*
		 * Doesn't have the flag
		 */
		if (!player.getEquipment().contains(opposite.getFlag().getId())) {
			return false;
		}
		/*
		 * Score
		 */
		if (CastleWarsManager.isSaradomin(player)) {
			CastleWarsManager.saradominScore++;
		} else if (CastleWarsManager.isZamorak(player)) {
			CastleWarsManager.zamorakScore++;
		}
		/*
		 * Set flag state
		 */
		opposite.setFlagDropped(false);
		opposite.setFlagTaken(false);
		/*
		 * Update flag
		 */
		updateFlag(opposite);
		updateFlag(team);
		/*
		 * Reset item
		 */
		player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(-1));
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		player.getEquipment().refreshItems();
		/*
		 * Scores notification
		 */
		for (Player p : CastleWarsManager.getAllPlayers()) {
			if (p == null) {
				continue;
			}
			p.getPacketSender().sendMessage(
					player.getUsername() + " has scored for " + Misc.formatPlayerName(team.getFlag().name()));
		}
		return true;
	}

	/**
	 * Updating the flag
	 * 
	 * @param team the team
	 */
	public static void updateFlag(CastleWarsTeam team) {
		/*
		 * The state
		 */
		String state = team.getFlagState().toLowerCase();
		/*
		 * The id
		 */
		int id = team.getFlag().getCapture();
		/*
		 * Other states
		 */
		if (state.contains("dropped")) {
			id = team.getFlag().getDropped();
		} else if (state.contains("taken")) {
			id = team.getFlag().getStand();
		}
		/*
		 * The object
		 */
		final GameObject object = new GameObject(id, team.getFlag().getPosition());
		/*
		 * Spawn object
		 */
		CustomObjects.spawnGlobalObject(object);
		team.getPlayers().stream().filter(Objects::nonNull).forEach(p -> p.getPacketSender()
				.debug("id: " + object.getId() + ",state: " + state, CastleWarsManager.TESTING));
	}
}
