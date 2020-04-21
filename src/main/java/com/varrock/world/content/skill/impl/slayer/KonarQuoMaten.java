package com.varrock.world.content.skill.impl.slayer;

import java.util.ArrayList;

import com.varrock.model.Skill;
import com.varrock.model.Locations.Location;
import com.varrock.model.definitions.NpcDefinition;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.PlayerPanel;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

/**
 * Handles the Konar Quo Maten Slayer master
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class KonarQuoMaten {

	/**
	 * The slayer tasks from Konar
	 */
	public enum SlayerTask {

		NO_TASK(-1, null, null, -1, null),

		ABERRANT_SPECTRE(1604, new Location[] { Location.SLAYER_TOWER, }, new int[] { 120, 170 }, 60,
				new String[] { "Deviant spectre" }),

		ABYSSAL_DEMON(1615, new Location[] { Location.SLAYER_TOWER, }, new int[] { 120, 170 }, 85,
				new String[] { "Abyssal sire" }),

		ADAMANT_DRAGON(23030, new Location[] {}, new int[] { 120, 170 }, 1, null),

		ANKOU(4382, new Location[] { Location.STRONGHOLD_OF_SECURITY, }, new int[] { 50, 50 }, 1, null),

		AVIANSIE(6246, new Location[] { Location.GODWARS_DUNGEON, }, new int[] { 120, 170 }, 1, null),

		BLACK_DEMON(84, new Location[] { Location.TAVERLEY_DUNGEON, Location.BRIMHAVEN_DUNGEON, },
				new int[] { 120, 170 }, 1, new String[] { "Skotizo" }),

		BLACK_DRAGON(54, new Location[] { Location.TAVERLEY_DUNGEON, }, new int[] { 10, 15 }, 1,
				new String[] { "Brutal black dragon", "Baby black dragon", "King black dragon", }),

		BLOODVELDS(1618, new Location[] { Location.GODWARS_DUNGEON, Location.SLAYER_TOWER, }, new int[] { 120, 170 },
				50, null),

		BLUE_DRAGON(55, new Location[] { Location.TAVERLEY_DUNGEON, }, new int[] { 120, 170 }, 1, null),

		BRONZE_DRAGON(1590, new Location[] { Location.BRIMHAVEN_DUNGEON, }, new int[] { 30, 50 }, 1, null),

		DAGANNOTH(1348, new Location[] { Location.LIGHTHOUSE, }, new int[] { 120, 170 }, 1,
				new String[] { "Dagannoth King", "Dagannoth fledgeling", "Dagannoth spawn", "dagannoth", }),

		DARK_BEAST(2783, new Location[] {}, new int[] { 10, 15 }, 90, null),

		DUST_DEVIL(1624, new Location[] {}, new int[] { 120, 170 }, 65, null),

		FIRE_GIANT(110, new Location[] { Location.WATERFALL_DUNGEON, }, new int[] { 120, 170 }, 1, null),

		GARGOYLES(1610, new Location[] { Location.SLAYER_TOWER, }, new int[] { 120, 170 }, 75, null),

		GREATER_DEMON(4698, new Location[] { Location.TAVERLEY_DUNGEON, Location.BRIMHAVEN_DUNGEON },
				new int[] { 120, 170 }, 1, new String[] { "Skotizo" }),

		HELLHOUND(49, new Location[] { Location.TAVERLEY_DUNGEON, }, new int[] { 120, 170 }, 1,
				new String[] { "Cerberus" }),

		HYDRA(23609, new Location[] { Location.KARUULM_SLAYER_DUNGEON }, new int[] { 125, 190 }, 95,
				new String[] { "Alchemical Hydra" }),

		IRON_DRAGON(1591, new Location[] { Location.BRIMHAVEN_DUNGEON, }, new int[] { 30, 50 }, 1, null),

		JELLIE(1637, new Location[] { Location.FREMENNIK_SLAYER_DUNGEON, }, new int[] { 120, 170 }, 52,
				new String[] { "Warped Jelly" }),

		KALPHITE(3589, new Location[] { Location.KALPHITE_LAIR, Location.KALPHITE_CAVE }, new int[] { 120, 170 }, 1,
				new String[] { "Kalphite Queen", "Kalphite Guardian", "Kalphite Worker", "Kalphite Soldier",
						"Kalphite guardian" }),

		KURASK(4229, new Location[] { Location.FREMENNIK_SLAYER_DUNGEON, }, new int[] { 120, 170 }, 70, null),

		LIZARDMEN(6766, new Location[] { Location.LIZARD_CANYON, }, new int[] { 90, 110 }, 1,
				new String[] { "Lizardman shaman", "Lizard canyon", "Lizardman sttlement", "Lizardman brute", }),

		MITHRIL_DRAGON(5363, new Location[] { Location.ANCIENT_CAVERN, }, new int[] { 3, 6 }, 1, null),

		MUTATED_ZYGOMITE(3346, new Location[] { Location.FOSSIL_ISLAND, }, new int[] { 10, 25 }, 57, null),

		NECHRYAEL(1613, new Location[] { Location.SLAYER_TOWER, }, new int[] { 110, 110 }, 80, null),

		RED_DRAGON(4669, new Location[] { Location.BRIMHAVEN_DUNGEON, }, new int[] { 30, 50 }, 1,
				new String[] { "Brutal Red Dragon", "Baby Red Dragon", }),

		RUNE_DRAGON(23031, new Location[] {}, new int[] { 3, 6 }, 1, null),

		SKELETAL_WYVREN(3071, new Location[] { Location.ASGARNIAN_ICE_DUNGEON, }, new int[] { 5, 12 }, 72, null),

		SMOKE_DEVIL(499, new Location[] { Location.SMOKE_DEVIL_DUNGEON, }, new int[] { 120, 170 }, 93,
				new String[] { "Thermonuclear smoke devil", }),

		STEEL_DRAGON(1592, new Location[] { Location.BRIMHAVEN_DUNGEON, }, new int[] { 30, 50 }, 1, null),

		TROLL(72, new Location[] { Location.TROLL_STRONGHOLD, }, new int[] { 120, 170 }, 1, new String[] { "Troll" }),

		TUROTH(1626, new Location[] { Location.FREMENNIK_SLAYER_DUNGEON, }, new int[] { 120, 170 }, 1, null),

		WATERFIEND(5361, new Location[] { Location.ANCIENT_CAVERN, }, new int[] { 120, 170 }, 55, null),

		WYVERM(-1, new Location[] {}, new int[] { 125, 190 }, 92, null),
		BRINE_RAT(-1, new Location[] {}, new int[] { 120, 170 }, 47, null),
		CAVE_KRAKEN(-1, new Location[] {}, new int[] { 80, 100 }, 87, new String[] { "Kraken" }),
		FOSSIL_ISLAND_WVYREN(-1, new Location[] {}, new int[] { 15, 30 }, 66,
				new String[] { "Spitting Wyvern", "Taloned wyvern", "Long-tailed wyvern" }),;

		/**
		 * The id
		 */
		private int id;

		/**
		 * The location
		 */
		private Location[] location;

		/**
		 * The amount
		 */
		private int[] amount;

		/**
		 * The slayer level
		 */
		private int slayerLevel;

		/**
		 * The alternative
		 */
		private String[] alternative;

		/**
		 * Represents a slayer task
		 * 
		 * @param id          the id
		 * @param location    the location
		 * @param amount      the amount
		 * @param slayerLevel the slayer level
		 * @param alternative the alternative monsters
		 */
		SlayerTask(int id, Location[] location, int[] amount, int slayerLevel, String[] alternative) {
			this.id = id;
			this.location = location;
			this.amount = amount;
			this.slayerLevel = slayerLevel;
			this.alternative = alternative;
		}

		/**
		 * Gets the id
		 *
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * Gets the location
		 *
		 * @return the location
		 */
		public Location[] getLocation() {
			return location;
		}

		/**
		 * Gets the amount
		 *
		 * @return the amount
		 */
		public int[] getAmount() {
			return amount;
		}

		/**
		 * Gets the slayerLevel
		 *
		 * @return the slayerLevel
		 */
		public int getSlayerLevel() {
			return slayerLevel;
		}

		/**
		 * Gets the alternative
		 *
		 * @return the alternative
		 */
		public String[] getAlternative() {
			return alternative;
		}

		/**
		 * Gets a task
		 * 
		 * @param player the player
		 * @return the task
		 */
		public static SlayerTask getTask(Player player) {
			ArrayList<SlayerTask> tasks = new ArrayList<SlayerTask>();
			for (SlayerTask task : values()) {
				if (task == SlayerTask.NO_TASK) {
					continue;
				}
				if (task.getId() == -1) {
					continue;
				}
				for (SlayerTasks t : SlayerTasks.values()) {
					if (t.getNpcIds().contains(task.getId())) {
						continue;
					}
				}
				if (World.getNpcById(task.getId()) == null) {
					continue;
				}
				if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) >= task.getSlayerLevel()) {
					tasks.add(task);
				}
			}
			if (tasks.size() < 1) {
				return SlayerTask.NO_TASK;
			}
			return tasks.get(Misc.getRandom(tasks.size() - 1));
		}
	}

	/**
	 * Getting an assignment
	 * 
	 * @param player the player
	 */
	public static void getAssignment(Player player) {
		/*
		 * Has task
		 */
		boolean hasTask = player.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK
				&& player.getSlayer().getLastTask() != player.getSlayer().getSlayerTask();
		/*
		 * Has duo partner
		 */
		boolean duoSlayer = player.getSlayer().getDuoPartner() != null;
		/*
		 * Has duo
		 */
		if (duoSlayer && !player.getSlayer().assignDuoSlayerTask())
			return;
		/*
		 * Has task
		 */
		if (hasTask || !player.getSlayer().getTask().equals(SlayerTask.NO_TASK)) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		/*
		 * The tasks
		 */
		SlayerTask task = SlayerTask.getTask(player);
		/*
		 * Found no task
		 */
		if (task == SlayerTask.NO_TASK) {
			player.getPacketSender().sendMessage("No task was found for you. Please try again.");
			return;
		}
		/*
		 * The amount
		 */
		int amount = Misc.inclusiveRandom(task.getAmount()[0], task.getAmount()[1]);
		/*
		 * The location
		 */
		Location location = task.getLocation().length > 0
				? task.getLocation()[Misc.exclusiveRandom(task.getLocation().length - 1)]
				: Location.WILDERNESS;
		/*
		 * Notify task
		 */
		player.getPacketSender()
				.sendMessage("Your task is to kill: " + NpcDefinition.forId(task.getId()).getName() + " x" + amount);
		/*
		 * Set task
		 */
		player.getSlayer().setTask(task);
		player.getSlayer().setAmountToSlay(amount);
		player.getSlayer().setSlayerLocation(location);
		/*
		 * Refresh panel
		 */
		PlayerPanel.refreshPanel(player);
		/*
		 * Set duo
		 */
		if (duoSlayer) {
			Player duo = World.getPlayerByName(player.getSlayer().getDuoPartner());
			duo.getSlayer().setTask(task);
			duo.getSlayer().setSlayerLocation(location);
			duo.getSlayer().setAmountToSlay(amount);
			duo.getPacketSender().sendInterfaceRemoval();
			PlayerPanel.refreshPanel(duo);
		}
		DialogueManager.start(player, SlayerDialogues.receivedTask(player, SlayerMaster.KONAR_QUO_MATEN, null));
	}

	/**
	 * Slaying the assignment
	 * 
	 * @param player the player
	 * @param npc    the npc
	 */
	public static void slainAssignment(Player player, NPC npc) {
		/*
		 * No task
		 */
		if (player.getSlayer().getTask().equals(SlayerTask.NO_TASK)) {
			return;
		}
		/*
		 * The task
		 */
		SlayerTask task = player.getSlayer().getTask();
		/*
		 * Alternative npcs
		 */
		boolean alternative = false;
		/*
		 * Checks for alternative
		 */
		if (task.getAlternative() != null) {
			for (String s : task.getAlternative()) {
				if (npc.getDefinition().getName().toLowerCase().contains(s.toLowerCase())) {
					alternative = true;
					break;
				}
			}
		}
		/*
		 * By enum name
		 */
		if (npc.getDefinition().getName().toLowerCase().contains(task.name().toLowerCase().replaceAll("_", " "))) {
			alternative = true;
		}
		/*
		 * Not found
		 */
		if (task.getId() != npc.getId() && !alternative) {
			return;
		}
		/*
		 * Wrong location
		 */
//		if (player.getSlayer().getSlayerLocation() != null) {
//			if (!player.getLocation().equals(player.getSlayer().getSlayerLocation())) {
//				player.getPacketSender().sendMessage("You killed your slayer assignment but at the wrong location.");
//				player.getPacketSender().sendMessage(
//						"You need to kill your assignment at " + player.getSlayer().getSlayerLocation().getName());
//				return;
//			}
//		}
		/*
		 * Slay
		 */
		int xp = npc.getDefinition().getCombatLevel() * (player.getSkillManager().getCombatLevel() / 2);
		player.getSkillManager().addExperience(Skill.SLAYER, player.getSlayer().doubleSlayerXP ? xp * 2 : xp);
		if (player.getSlayer().getAmountToSlay() > 1) {
			player.getSlayer().setAmountToSlay(player.getSlayer().getAmountToSlay() - 1);
		} else {
			player.getPacketSender().sendMessage("")
					.sendMessage("You've completed your Slayer task! Return to a Slayer master for another one.");
			player.getSlayer().setTaskStreak(player.getSlayer().getTaskStreak() + 1);
			player.getSkillManager().addExperience(Skill.SLAYER, xp * 2);
			player.getSlayer().setSlayerTask(SlayerTasks.NO_TASK);
			player.getSlayer().setAmountToSlay(0);
			player.getSlayer().setTask(SlayerTask.NO_TASK);
			player.getSlayer().givePoints(SlayerMaster.KONAR_QUO_MATEN);
		}
		/*
		 * Refresh panel
		 */
		PlayerPanel.refreshPanel(player);
	}
}
