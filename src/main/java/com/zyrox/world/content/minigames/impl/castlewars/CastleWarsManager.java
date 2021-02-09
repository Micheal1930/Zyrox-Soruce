package com.zyrox.world.content.minigames.impl.castlewars;

import static com.zyrox.model.Locations.Location.CASTLE_WARS_GAME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import com.zyrox.GameSettings;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.Boundary;
import com.zyrox.model.Flag;
import com.zyrox.model.Item;
import com.zyrox.model.Position;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.Consumables;
import com.zyrox.world.content.minigames.impl.castlewars.item.flag.CastleWarsFlag;
import com.zyrox.world.content.minigames.impl.castlewars.item.flag.CastleWarsFlagManager;
import com.zyrox.world.content.minigames.impl.castlewars.npc.lanthus.CastleWarsLanthus;
import com.zyrox.world.content.minigames.impl.castlewars.object.CastleWarsDoubleDoor;
import com.zyrox.world.content.minigames.impl.castlewars.object.CastleWarsSingleDoor;
import com.zyrox.world.content.minigames.impl.castlewars.object.CastleWarsSteppingStone;
import com.zyrox.world.content.minigames.impl.castlewars.object.catapult.CastleWarsCatapultManager;
import com.zyrox.world.content.minigames.impl.castlewars.object.rocks.CastleWarsCollapsingRockManager;
import com.zyrox.world.content.minigames.impl.castlewars.team.CastleWarsTeam;
import com.zyrox.world.content.minigames.impl.castlewars.team.impl.SaradominCastleWarsTeam;
import com.zyrox.world.content.minigames.impl.castlewars.team.impl.ZamorakCastleWarsTeam;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Handles Castle Wars
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class CastleWarsManager {

	// TODO:
	/*
	 * 
	 * 1. Climbing rope
	 * 
	 * 2. Lanthus arrow collection
	 * 
	 * -----
	 *
	 * 
	 */

	/**
	 * Testing the minigame - disable for real game
	 */
	public static boolean TESTING = false;

	/**
	 * The minigame
	 */
	public static final CastleWarsMinigame MINIGAME = new CastleWarsMinigame();

	/**
	 * The zamorak team
	 */
	public static ZamorakCastleWarsTeam zamorak = new ZamorakCastleWarsTeam();

	/**
	 * The saradomin team
	 */
	public static SaradominCastleWarsTeam saradomin = new SaradominCastleWarsTeam();

	/**
	 * The lobby position
	 */
	public static final Position MAIN_LOBBY = new Position(2441, 3091, 0);

	/**
	 * The looting animation
	 */
	private static final Animation LOOT = new Animation(881);

	/**
	 * The bronze pickaxe
	 */
	public static final Item BRONZE_PICK_AXE = new Item(1265);

	/**
	 * The explosive potion
	 */
	public static final Item EXPLOSIVE_POTION = new Item(4045);

	/**
	 * The barricades
	 */
	public static final Item BARRICADE = new Item(4053);

	/**
	 * The bandages
	 */
	public static final Item BANDAGE = new Item(4049);

	/**
	 * The tinderbox
	 */
	public static final Item TINDERBOX = new Item(590);

	/**
	 * The rope
	 */
	private static final Item ROPE = new Item(954);

	/**
	 * The rock
	 */
	public static final Item ROCK = new Item(4043);

	/**
	 * The castle wars ticket
	 */
	public static final Item CASTLE_WARS_TICKET = new Item(4067);

	/**
	 * The safe areas in the game
	 */
	public static final ArrayList<Boundary> SAFE_AREAS = new ArrayList<Boundary>(
			Arrays.asList(new Boundary(new Position(2368, 3127, 1), new Position(2376, 3135, 1)),
					new Boundary(new Position(2423, 3072, 1), new Position(3431, 3080, 1))));

	/**
	 * The castle wars items
	 */
	private static final Item[] CASTLE_WAR_ITEMS = { EXPLOSIVE_POTION, BARRICADE, BANDAGE, new Item(4039),
			new Item(4037), };

	/**
	 * Zamorak related items
	 */
	private static final String[] ZAMORAK_ITEMS = { "Staff of the dead", "Unholy symbol", "Unholy book", "Zamorak",
			"Zamorakian", };

	/**
	 * Saradomin related items
	 */
	private static final String[] SARADOMIN_ITEMS = { "Holy symbol", "Holy book", "Monk's robes", "Holy sandals",
			"Holy blessing", "Saradomin", };

	/**
	 * Guthix related items
	 */
	private static final String[] GUTHIX_ITEMS = { "Book of balance", "Peaceful blessing", "Void knight",
			"Druid's robes", "Silver sickle", "Guthix", };

	/**
	 * The minimum players on each team
	 */
	private static final int MINIMUM_PLAYERS = 3;

	/**
	 * The waiting interface id
	 */
	private static final int WAITING_INTERFACE_ID = 11_479;

	/**
	 * The main game screen interface id
	 */
	private static final int MAIN_GAME_INTERFACE_ID = 11_146;

	/**
	 * The duration of the game
	 */
	private static final int GAME_DURATION = 20;

	/**
	 * The waiting time
	 */
	private static final int WAIT_DURATION = 2;

	/**
	 * The sheep id
	 */
	private static final int SHEEP = 1529;

	/**
	 * The rabbit id
	 */
	private static final int RABBIT = 1530;

	/**
	 * The imp id
	 */
	private static final int IMP = 1531;

	/**
	 * The game is running
	 */
	private static boolean running;

	/**
	 * The game time
	 */
	private static int gameTime;

	/**
	 * The waiting time
	 */
	private static int waitTime;

	/**
	 * The zamorak score
	 */
	public static int zamorakScore;

	/**
	 * The saradomin score
	 */
	public static int saradominScore;

	/**
	 * Start the castle wars cycle
	 */
	public static void init() {
		/*
		 * Set the times
		 */
		waitTime = WAIT_DURATION;
		gameTime = GAME_DURATION;
		/*
		 * Lanthus
		 */
		NPC lanthus = World.getNpcById(CastleWarsLanthus.LANTHUS);
		/*
		 * Invalid lanthus
		 */
		if (lanthus == null) {
			System.out.println("~Castle Wars Lanthus invalid");
		}
		/*
		 * Start the main game cycle
		 */
		TaskManager.submit(new Task(TESTING ? 10 : 100) {

			@Override
			protected void execute() {
				/*
				 * Game is running
				 */
				if (running) {
					/*
					 * Decrease game time
					 */
					if (gameTime > 0) {
						if (!TESTING) {
							gameTime--;
						}
						/*
						 * Game has ended
						 */
					} else if (gameTime == 0) {
						end();
						/*
						 * Lanthus notify
						 */
						if (lanthus != null) {
							lanthus.forceChat(
									"A Castle Wars game has ENDED! A new game in " + WAIT_DURATION + " minutes!");
						}
						waitTime = WAIT_DURATION;
						running = false;
					}
				} else {
					/*
					 * Enough players
					 */
					if (!enoughPlayers()) {
						if (waitTime < WAIT_DURATION) {
							waitTime = WAIT_DURATION;
						}
					} else {
						/*
						 * Decrease waiting time
						 */
						if (waitTime > 0) {
							waitTime -= 1;
							/*
							 * Lanthus notify
							 */
							if (lanthus != null) {
								lanthus.forceChat("A Castle Wars game is starting in " + waitTime + " minutes!");
							}
							/*
							 * Start the game
							 */
						} else if (waitTime == 0) {
							running = true;
							gameTime = GAME_DURATION;
							/*
							 * Lanthus notify
							 */
							if (lanthus != null) {
								lanthus.forceChat("A Castle Wars game has STARTED!");
							}
							/*
							 * Start the minigame
							 */
							start();
						}
					}
				}
			}
		});
	}

	/**
	 * Starting the minigame
	 */
	private static void start() {
		/*
		 * Prepare the game
		 */
		MINIGAME.prepare();
		/*
		 * Reset scores
		 */
		saradominScore = 0;
		zamorakScore = 0;
		/*
		 * Add zamorak players
		 */
		for (Player player : zamorak.getPlayers()) {
			if (player.getNpcTransformationId() > 0) {
				player.setNpcTransformationId(-1);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			}
			player.moveTo(zamorak.getPrepRoom());
		}
		/*
		 * Add saradomin players
		 */
		for (Player player : saradomin.getPlayers()) {
			if (player.getNpcTransformationId() > 0) {
				player.setNpcTransformationId(-1);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			}
			player.moveTo(saradomin.getPrepRoom());
		}
	}

	/**
	 * End of the game
	 */
	public static void end() {
		/*
		 * Award
		 */
		if (zamorakScore > saradominScore) {
			awardTickets(zamorakScore, saradominScore, zamorak, saradomin);
		} else {
			awardTickets(saradominScore, zamorakScore, saradomin, zamorak);
		}
		/*
		 * Leave
		 */
		for (Player player : getAllPlayers()) {
			if (player == null) {
				continue;
			}
			leave(player, true);
		}
	}

	/**
	 * Awarading tickets
	 * 
	 * @param winnerScore the winner score
	 * @param loserScore  the loser score
	 * @param winnerTeam  the winner team
	 * @param loserTeam   the loser team
	 * @return award
	 */
	private static void awardTickets(int winnerScore, int loserScore, CastleWarsTeam winnerTeam,
			CastleWarsTeam loserTeam) {
		/*
		 * Regular win
		 */
		Predicate<Player> ticketFilter = p -> p != null  && Location.inLocation(p, CASTLE_WARS_GAME);
		if (loserScore == 0 && winnerScore > 0) {
			winnerTeam.getPlayers().stream().filter(ticketFilter)
					.forEach(p -> p.getInventory().add(CASTLE_WARS_TICKET.getId(), 3));
			loserTeam.getPlayers().stream().filter(ticketFilter)
					.forEach(p -> p.getInventory().add(CASTLE_WARS_TICKET.getId(), 1));
		} else if (loserScore == 1 && winnerScore > 1) {
			/*
			 * Scoreless win
			 */
			winnerTeam.getPlayers().stream().filter(ticketFilter)
					.forEach(p -> p.getInventory().add(CASTLE_WARS_TICKET.getId(), 2));
			loserTeam.getPlayers().stream().filter(ticketFilter)
					.forEach(p -> p.getInventory().add(CASTLE_WARS_TICKET.getId(), 1));
		} else if (loserScore == 0 && winnerScore == 0) {
			/*
			 * Regular tie
			 */
			winnerTeam.getPlayers().stream().filter(ticketFilter)
					.forEach(p -> p.getInventory().add(CASTLE_WARS_TICKET.getId(), 2));
			loserTeam.getPlayers().stream().filter(ticketFilter)
					.forEach(p -> p.getInventory().add(CASTLE_WARS_TICKET.getId(), 2));
		} else if (loserScore == 1 && winnerScore == 1) {
			winnerTeam.getPlayers().stream().filter(ticketFilter)
					.forEach(p -> p.getInventory().add(CASTLE_WARS_TICKET.getId(), 1));
			loserTeam.getPlayers().stream().filter(ticketFilter)
					.forEach(p -> p.getInventory().add(CASTLE_WARS_TICKET.getId(), 1));
		} else {
			winnerTeam.getPlayers().stream().filter(ticketFilter)
					.forEach(p -> p.getInventory().add(CASTLE_WARS_TICKET.getId(), 2));
			loserTeam.getPlayers().stream().filter(ticketFilter)
					.forEach(p -> p.getInventory().add(CASTLE_WARS_TICKET.getId(), 1));
		}
	}

	/**
	 * Joining a team
	 * 
	 * @param player the player
	 * @param team   the team
	 * @param type   the portal type
	 */
	private static void joinTeam(Player player, CastleWarsTeam team, int type) {
		/*
		 * Has cape
		 */
		if (!player.getEquipment().isEmpty(Equipment.CAPE_SLOT)) {
			player.getPacketSender().sendMessage("You cannot bring a cape into the minigame.");
			return;
		}
		/*
		 * Has helm
		 */
		if (!player.getEquipment().isEmpty(Equipment.HEAD_SLOT)) {
			player.getPacketSender().sendMessage("You cannot bring a helm into the minigame.");
			return;
		}
		/*
		 * Check restricted items
		 */
		for (Item item : Misc.concat(player.getInventory(), player.getEquipment()).getValidItems()) {
			/*
			 * Invalid item
			 */
			if (item == null) {
				continue;
			}
			/*
			 * Restricted item
			 */
			for (int restricted : GameSettings.OVERPOWERED_WEAPONS) {
				if (item.getId() == restricted) {
					player.getPacketSender()
							.sendMessage("You are carrying illegal items: " + item.getDefinition().getName());
					return;
				}
			}
		}
		/*
		 * Check inventory items
		 */
		for (Item item : player.getInventory().getItems()) {
			/*
			 * Invalid item
			 */
			if (item == null) {
				continue;
			}
			/*
			 * Found item
			 */
			if (item.getDefinition().getEquipmentSlot() == Equipment.HEAD_SLOT
					|| item.getDefinition().getEquipmentSlot() == Equipment.CAPE_SLOT) {
				player.getPacketSender().sendMessage(
						"You have a helm or a cape in your inventory which you cannot bring in to the minigame");
				return;
			}
			/*
			 * Check food
			 */
			if (Consumables.isFood(player, item.getId())) {
				player.getPacketSender().sendMessage("You can not bring any food into the minigame.");
				return;
			}
		}
		/*
		 * Has pet
		 */
		if (player.getSummoning().getFamiliar() != null) {
			player.getPacketSender().sendMessage("You can not bring a familiar into the minigame.");
			return;
		}
		/*
		 * Checks if on a team
		 */
		if (!team.getPlayers().contains(player)) {
			team.getPlayers().add(player);
		}
		/*
		 * Join the waiting room
		 */
		player.moveTo(team.getLobby());
		/*
		 * Set the cloak
		 */
		player.getEquipment().set(Equipment.CAPE_SLOT, team.getCloak());
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		player.getEquipment().refreshItems();
		/*
		 * Transform
		 */
		transformToNpc(player, type);
		/*
		 * Testing send straight to main game
		 */
		if (TESTING) {
			player.moveTo(team.getPrepRoom());
		}
	}

	/**
	 * Joining the guthix team
	 * 
	 * @param player the player
	 */
	private static void joinGuthix(Player player) {
		/**
		 * Checks the size
		 */
		if (saradomin.getPlayers().size() > zamorak.getPlayers().size()) {
			joinTeam(player, zamorak, 1);
		} else {
			joinTeam(player, saradomin, 1);
		}
	}

	/**
	 * Handles death in castle wars
	 * 
	 * @param player the player
	 */
	public static void handleDeath(Player player) {
		/*
		 * Not in castle wars
		 */
		if (!inCastleWars(player)) {
			return;
		}
		/*
		 * Holding flag
		 */
		if (isHoldingFlag(player)) {
			CastleWarsFlagManager.dropFlag(player);
		}
		/*
		 * The team
		 */
		CastleWarsTeam team = getTeam(player);
		/*
		 * Send to prep room
		 */
		player.delayedMoveTo(team.getPrepRoom(), 1);
	}

	/**
	 * Leaving the minigame
	 * 
	 * @param player  the player
	 * @param endGame whether the game has ended
	 */
	public static void leave(Player player, boolean endGame) {
		/*
		 * The team
		 */
		CastleWarsTeam team = getTeam(player);
		/*
		 * Move to main lobby
		 */
		player.getPacketSender().sendInterfaceRemoval();
		/*
		 * Remove items
		 */
		for (Item item : CASTLE_WAR_ITEMS) {
			player.getInventory().delete(item.getId(), Integer.MAX_VALUE);
		}
		/*
		 * From game to lobby
		 */
		if (player.getLocation().equals(CASTLE_WARS_GAME) && team != null && !endGame) {
			player.moveTo(team.getLobby());
		} else {
			/*
			 * Has a team
			 */
			if (team != null) {
				team.getPlayers().remove(player);
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(-1));
				/*
				 * Holding flag
				 */
				if (isHoldingFlag(player)) {
					player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(-1));
				}
				player.getEquipment().refreshItems();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			}
			/*
			 * Move to main lobby
			 */
			player.moveTo(MAIN_LOBBY);
		}
		/*
		 * Untransform
		 */
		player.setNpcTransformationId(-1);
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		/*
		 * Remove attack option
		 */
		player.getPacketSender().sendInteractionOption("null", 2, true);
		/*
		 * Reset combat
		 */
		player.getCombatBuilder().reset(true);
		/*
		 * Reset movement
		 */
		player.getMovementQueue().reset();
		/*
		 * Reset minigame state
		 */
		player.setMinigame(null);
	}

	/**
	 * Transforming to npc based on items
	 * 
	 * @param player the player
	 * @param type   the god type
	 */
	private static void transformToNpc(Player player, int type) {
		/*
		 * Loop through items
		 */
		for (Item item : player.getInventory().getItems()) {
			/*
			 * Invalid item
			 */
			if (item == null) {
				continue;
			}
			/*
			 * The item name
			 */
			String name = item.getDefinition().getName().toLowerCase();
			/*
			 * Check saradomin
			 */
			if (type == 0) {
				/*
				 * Check zamorak and guthix items
				 */
				for (String s : Misc.concat(ZAMORAK_ITEMS, GUTHIX_ITEMS)) {
					if (name.contains(s.toLowerCase())) {
						player.setNpcTransformationId(RABBIT);
						break;
					}
				}
			} else
			/*
			 * Check guthix
			 */
			if (type == 1) {
				/*
				 * Check zamorak and saradomin items
				 */
				for (String s : Misc.concat(ZAMORAK_ITEMS, SARADOMIN_ITEMS)) {
					if (name.contains(s.toLowerCase())) {
						player.setNpcTransformationId(SHEEP);
						break;
					}
				}
			} else
			/*
			 * Check Zamorak
			 */
			if (type == 2) {
				/*
				 * Check saradomin and guthix items
				 */
				for (String s : Misc.concat(GUTHIX_ITEMS, SARADOMIN_ITEMS)) {
					if (name.contains(s.toLowerCase())) {
						player.setNpcTransformationId(IMP);
						break;
					}
				}
			}
		}
		/*
		 * Refresh
		 */
		if (player.getNpcTransformationId() > 0) {
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendMessage("You feel very strange..");
		}
	}

	/**
	 * Sending the waiting interface
	 * 
	 * @param player the player
	 */
	public static void sendWaitingInterface(Player player) {
		String string = "";
		/*
		 * Enough players
		 */
		if (enoughPlayers()) {
			string = "Game starting in: " + waitTime + " minutes.";
			if (waitTime == 0) {
				string = "The game is starting in less than a minute! PREPARE!";
			}
		} else if (isZamorak(player)) {
			if (saradomin.getPlayers().size() < MINIMUM_PLAYERS) {
				string = "Waiting for players to join the other team.";
			} else if (zamorak.getPlayers().size() < MINIMUM_PLAYERS) {
				string = "Waiting for players to join the team.";
			}
		} else if (isSaradomin(player)) {
			if (zamorak.getPlayers().size() < MINIMUM_PLAYERS) {
				string = "Waiting for players to join the other team.";
			} else if (saradomin.getPlayers().size() < MINIMUM_PLAYERS) {
				string = "Waiting for players to join the team.";
			}
		}
		/*
		 * In progress
		 */
		if (running) {
			string = "Game is in progress: " + gameTime + " minutes left.";
		}

		string += "\\n\\n";
		string += "Saradomin players: " + saradomin.getPlayers().size() + "\\n";
		string += "Zamorak players: " + zamorak.getPlayers().size();
		player.getPacketSender().sendString(WAITING_INTERFACE_ID + 1, string);
		player.sendParallellInterfaceVisibility(WAITING_INTERFACE_ID, true);
		player.sendParallellInterfaceVisibility(MAIN_GAME_INTERFACE_ID, false);
	}

	/**
	 * Sending in game interface
	 * 
	 * @param player the player
	 */
	public static void sendIngameInterface(Player player) {
		if (isSaradomin(player)) {
			sendIngameInterface(player, saradomin);
		} else if (isZamorak(player)) {
			sendIngameInterface(player, zamorak);
		}
	}

	/**
	 * Sends the castle wars interface
	 * 
	 * @param team   the team
	 * @param config the config id
	 */
	private static void sendIngameInterface(Player player, CastleWarsTeam team) {
		/*
		 * The minigame
		 */
		CastleWarsMinigame minigame = CastleWarsManager.MINIGAME;
		/*
		 * No minigame
		 */
		if (minigame == null) {
			return;
		}
		/*
		 * Show time left
		 */
		player.getPacketSender().sendString(11155, gameTime + " Min");
		/*
		 * Show score
		 */
		player.getPacketSender().sendString(11147, "Zamorak = " + zamorakScore); // left score
		player.getPacketSender().sendString(11148, saradominScore + " = Saradomin"); // middle score
		/*
		 * Show door
		 */
		player.getPacketSender().sendString(11158, team.isDoorLocked() ? "@gre@Locked" : "@red@Unlocked");
		/*
		 * Show rock state
		 */
		if (isSaradomin(player)) {
			player.getPacketSender().sendString(11160,
					minigame.getRocksCollapsed()[0] ? "@gre@Collapsed" : "@red@Collapsed");
			player.getPacketSender().sendString(11162,
					minigame.getRocksCollapsed()[1] ? "@gre@Collapsed" : "@red@Collapsed");
		} else if (isZamorak(player)) {
			player.getPacketSender().sendString(11160,
					minigame.getRocksCollapsed()[2] ? "@gre@Collapsed" : "@red@Collapsed");
			player.getPacketSender().sendString(11162,
					minigame.getRocksCollapsed()[3] ? "@gre@Collapsed" : "@red@Collapsed");
		}
		/*
		 * Catapult
		 */
		player.getPacketSender().sendString(11164, team.isOperational() ? "@gre@Operational" : "@red@Operational");
		/*
		 * Show flag state
		 */
		String saraFlag = saradomin.getFlagState();
		String zaraFlag = zamorak.getFlagState();
		player.getPacketSender().sendString(11166,
				!saraFlag.contains("Safe") ? "@whi@||||||||| " + saraFlag : saraFlag);
		player.getPacketSender().sendString(11168,
				!zaraFlag.contains("Safe") ? "@whi@||||||||| " + zaraFlag : zaraFlag);
		player.sendParallellInterfaceVisibility(MAIN_GAME_INTERFACE_ID, true);
	}

	/**
	 * Checking for enough players
	 * 
	 * @return enough players
	 */
	private static boolean enoughPlayers() {
		return TESTING ? true
				: (zamorak.getPlayers().size() >= MINIMUM_PLAYERS && saradomin.getPlayers().size() >= MINIMUM_PLAYERS);
	}

	/**
	 * Checks if is on saradomin team
	 * 
	 * @param player the player
	 * @return saradomin team
	 */
	public static boolean isSaradomin(Player player) {
		return saradomin.getPlayers().contains(player);
	}

	/**
	 * Checks if is on zamorak team
	 * 
	 * @param player the player
	 * @return zamorak team
	 */
	public static boolean isZamorak(Player player) {
		return zamorak.getPlayers().contains(player);
	}

	/**
	 * Gets the team
	 * 
	 * @param player the player
	 * @return the team
	 */
	public static CastleWarsTeam getTeam(Player player) {
		/*
		 * Testing
		 */
		if (TESTING) {
//			return zamorak;
		}
		return isSaradomin(player) ? saradomin : isZamorak(player) ? zamorak : null;
	}

	/**
	 * Gets the opposite team
	 * 
	 * @param player the player
	 * @return the opposite team
	 */
	public static CastleWarsTeam getOppositeTeam(Player player) {
		/*
		 * Testing
		 */
		if (TESTING) {
//			return saradomin;
		}
		return isSaradomin(player) ? zamorak : isZamorak(player) ? saradomin : null;
	}

	/**
	 * Gets the team by flag
	 * 
	 * @param id the flag id
	 * @return the team
	 */
	public static CastleWarsTeam getTeamForFlag(CastleWarsFlag flag) {
		return flag.equals(CastleWarsFlag.SARADOMIN) ? saradomin : flag.equals(CastleWarsFlag.ZAMORAK) ? zamorak : null;
	}

	/**
	 * Checking whether on the same team
	 * 
	 * @param player      the player
	 * @param otherPlayer the other player
	 * @return on the same team
	 */
	public static boolean sameTeam(Player player, Player otherPlayer) {
		return getTeam(player) != null && getTeam(otherPlayer) != null && getTeam(player) == getTeam(otherPlayer);
	}

	/**
	 * Checking whether holding flag
	 * 
	 * @param player the player
	 * @return holding flag
	 */
	public static boolean isHoldingFlag(Player player) {
		return player.getEquipment().contains(saradomin.getFlag().getId())
				|| player.getEquipment().contains(zamorak.getFlag().getId());
	}

	/**
	 * Checking if in castle wars
	 * 
	 * @param player the player
	 * @return in castle wars
	 */
	public static boolean inCastleWars(Player player) {
		return player.getLocation().equals(CASTLE_WARS_GAME)
				|| player.getLocation().equals(Location.CASTLE_WARS_WAITING_LOBBY);
	}

	/**
	 * Gets all the players in the game
	 * 
	 * @return the players
	 */
	public static ArrayList<Player> getAllPlayers() {
		/*
		 * The list of players
		 */
		ArrayList<Player> players = new ArrayList<Player>();
		/*
		 * All the possible victims
		 */
		players.addAll(zamorak.getPlayers());
		players.addAll(saradomin.getPlayers());
		return players;
	}

	/**
	 * Handles objects in Castle Wars minigame
	 * 
	 * @param player the player
	 * @param id     the id of the object
	 * @param x      the x of the object
	 * @param y      the y of the object
	 * @param type   the interaction type
	 * @return object interaction
	 */
	public static boolean handleObjects(Player player, int id, int x, int y, int type) {
		/*
		 * Outside the game
		 */
		if (player.getLocation().equals(Location.CASTLE_WARS_OUTGAME_LOBBY)) {
			switch (id) {
			case 4388: // zamorak portal
				joinTeam(player, zamorak, 2);
				return true;
			case 4387: // saradomin portal
				joinTeam(player, saradomin, 0);
				return true;
			case 4408: // guthix portal
				joinGuthix(player);
				return true;
			}
			return false;
			/*
			 * In lobby
			 */
		} else if (player.getLocation().equals(Location.CASTLE_WARS_WAITING_LOBBY)) {
			switch (id) {
			case 4390:
			case 4389:
				leave(player, false);
				return true;
			}
			return false;
			/*
			 * In game
			 */
		} else if (player.getLocation().equals(CASTLE_WARS_GAME)) {
			/*
			 * Handles the double door
			 */
			if (CastleWarsDoubleDoor.handleDoor(player, id, x, y, type)) {
				return true;
			}
			/*
			 * Handles the single door
			 */
			if (CastleWarsSingleDoor.handleDoor(player, id, x, y)) {
				return true;
			}
			/*
			 * Handles catapult
			 */
			if (CastleWarsCatapultManager.handleObject(player, id, x, y)) {
				return true;
			}
			switch (id) {
			case 4902:
			case 4903:
				CastleWarsFlagManager.captureFlag(player, id, x, y);
				return true;
			case 4901:
			case 4900:
				CastleWarsFlagManager.takeFlag(player, id, x, y);
				return true;
			case 4378:
			case 4377:
				CastleWarsFlagManager.score(player, id);
				return true;
			case CastleWarsCollapsingRockManager.CAVE_WALL:
				CastleWarsCollapsingRockManager.collapse(player, true);
				return true;
			case CastleWarsCollapsingRockManager.FULL_ROCKS:
			case CastleWarsCollapsingRockManager.HALF_ROCKS:
				CastleWarsCollapsingRockManager.breakRock(player, id, true);
				return true;
			case CastleWarsSteppingStone.STEPPING_STONE:
				CastleWarsSteppingStone.handleSteppingStone(player, id, x, y);
				return true;
			case 4469:
				if (isZamorak(player)) {
					player.getPacketSender().sendMessage("You can't enter the other teams prep room.");
					return true;
				}
				if (isHoldingFlag(player)) {
					player.getPacketSender().sendMessage("You can't go into the prep room with the flag!");
					return true;
				}
				if (x == 2426) {
					if (player.getPosition().getY() == 3080) {
						player.getMovementQueue().walkStep(0, 1);
						player.getPacketSender().sendInteractionOption("Attack", 2, true);
					} else if (player.getPosition().getY() == 3081) {
						player.getMovementQueue().walkStep(0, -1);
						player.getPacketSender().sendInteractionOption("null", 2, true);
					}
				} else if (x == 2422) {
					if (player.getPosition().getX() == 2422) {
						player.getMovementQueue().walkStep(1, 0);
						player.getPacketSender().sendInteractionOption("null", 2, true);
					} else if (player.getPosition().getX() == 2423) {
						player.getMovementQueue().walkStep(-1, 0);
						player.getPacketSender().sendInteractionOption("Attack", 2, true);
					}
				}
				return true;
			case 4470:
				if (isSaradomin(player)) {
					player.getPacketSender().sendMessage("You can't enter the other teams prep room.");
					return true;
				}
				if (isHoldingFlag(player)) {
					player.getPacketSender().sendMessage("You can't go into the prep room with the flag!");
					return true;
				}
				if (x == 2373 && y == 3126) {
					if (player.getPosition().getY() == 3126) {
						player.getMovementQueue().walkStep(0, 1);
						player.getPacketSender().sendInteractionOption("null", 2, true);
					} else if (player.getPosition().getY() == 3127) {
						player.getMovementQueue().walkStep(0, -1);
						player.getPacketSender().sendInteractionOption("Attack", 2, true);
					}
				} else if (x == 2377 && y == 3131) {
					if (player.getPosition().getX() == 2376) {
						player.getMovementQueue().walkStep(1, 0);
						player.getPacketSender().sendInteractionOption("Attack", 2, true);
					} else if (player.getPosition().getX() == 2377) {
						player.getMovementQueue().walkStep(-1, 0);
						player.getPacketSender().sendInteractionOption("null", 2, true);
					}
				}
				return true;
			case 4417:
				if (x == 2428 && y == 3081 && player.getPosition().getZ() == 1) {
					player.delayedMoveTo(new Position(2430, 3080, 2), 1);
				} else if (x == 2425 && y == 3074 && player.getPosition().getZ() == 2) {
					player.moveTo(new Position(2426, 3074, 3));
					CastleWarsFlagManager.updateFlag(saradomin);
				} else if (x == 2419 && y == 3078 && player.getPosition().getZ() == 0) {
					player.delayedMoveTo(new Position(2420, 3080, 1), 1);
				}
				return true;
			case 4418:
				if (x == 2380 && y == 3127 && player.getPosition().getZ() == 0) {
					player.delayedMoveTo(new Position(2379, 3127, 1), 1);
				} else if (x == 2374 && y == 3131 && player.getPosition().getZ() == 2) {
					player.moveTo(new Position(2373, 3133, 3));
					CastleWarsFlagManager.updateFlag(zamorak);
				} else if (x == 2369 && y == 3126 && player.getPosition().getZ() == 1) {
					player.delayedMoveTo(new Position(2369, 3127, 2), 1);
				}
				return true;
			case 4415:
				if (x == 2419 && y == 3080 && player.getPosition().getZ() == 1) {
					player.delayedMoveTo(new Position(2419, 3077, 0), 1);
				} else if (x == 2430 && y == 3081 && player.getPosition().getZ() == 2) {
					player.delayedMoveTo(new Position(2427, 3081, 1), 1);
				} else if (x == 2425 && y == 3074 && player.getPosition().getZ() == 3) {
					player.delayedMoveTo(new Position(2425, 3077, 2), 1);
				} else if (x == 2374 && y == 3133 && player.getPosition().getZ() == 3) {
					player.delayedMoveTo(new Position(2374, 3130, 2), 1);
				} else if (x == 2369 && y == 3126 && player.getPosition().getZ() == 2) {
					player.delayedMoveTo(new Position(2372, 3126, 1), 1);
				} else if (x == 2380 && y == 3127 && player.getPosition().getZ() == 1) {
					player.delayedMoveTo(new Position(2380, 3130, 0), 1);
				}
				return true;
			case 4419:
				if (x == 2417 && y == 3074 && player.getPosition().getZ() == 0) {
					if (player.getPosition().getX() == 2416) {
						player.delayedMoveTo(new Position(2417, 3077, 0), 1);
					} else {
						player.delayedMoveTo(new Position(2416, 3074, 0), 1);
					}
				} else if (x == 2417 && y == 3074 && player.getPosition().getZ() == 1) {
					player.delayedMoveTo(new Position(2417, 3073, 0), 0);
				}
				return true;
			case 4911:
				if (x == 2421 && y == 3073 && player.getPosition().getZ() == 1) {
					player.delayedMoveTo(new Position(2421, 3074, 0), 1);
				} else if (x == 2378 && y == 3134 && player.getPosition().getZ() == 1) {
					player.delayedMoveTo(new Position(2378, 3133, 0), 1);
				}
				return true;
			case 1747:
				if (x == 2421 && y == 3073 && player.getPosition().getZ() == 0) {
					player.delayedMoveTo(new Position(2421, 3074, 1), 1);
				} else if (x == 2378 && y == 3134 && player.getPosition().getZ() == 0) {
					player.delayedMoveTo(new Position(2378, 3133, 1), 1);
				}
				return true;
			case 4912:
				if (x == 2430 && y == 3082 && player.getPosition().getZ() == 0) {
					player.delayedMoveTo(
							new Position(player.getPosition().getX(), player.getPosition().getY() + 6400, 0), 1);
				} else if (x == 2369 && y == 3125 && player.getPosition().getZ() == 0) {
					player.delayedMoveTo(
							new Position(player.getPosition().getX(), player.getPosition().getY() + 6400, 0), 1);
				}
				return true;
			case 1757:
				if (x == 2400 && y == 9508) {
					player.delayedMoveTo(new Position(2400, 3107, 0), 1);
				} else if (x == 2399 && y == 9499) {
					player.delayedMoveTo(new Position(2399, 3100, 0), 1);
				} else if (x == 2430 && y == 9482) {
					player.delayedMoveTo(new Position(2430, 3081, 0), 1);
				} else {
					player.delayedMoveTo(new Position(2369, 3126, 0), 1);
				}
				return true;

			case 4420:
				if (x == 2382 && y == 3131 && player.getPosition().getZ() == 0) {
					if (player.getPosition().getX() >= 2383 && player.getPosition().getX() <= 2385) {
						player.delayedMoveTo(new Position(2382, 3130, 0), 1);
					} else {
						player.delayedMoveTo(new Position(2383, 3133, 0), 1);
					}
				}
				return true;
			case 1568:
				if (x == 2399 && y == 3099) {
					player.delayedMoveTo(new Position(2399, 9500, 0), 1);
				} else {
					player.delayedMoveTo(new Position(2400, 9507, 0), 1);
				}
				return true;
			case 4472:
				if (isZamorak(player) && !isHoldingFlag(player)) {
					player.delayedMoveTo(new Position(2370, 3132, 1), 1);
				}
				return true;
			case 4471:
				if (isSaradomin(player) && !isHoldingFlag(player)) {
					player.delayedMoveTo(new Position(2429, 3075, 1), 1);
				}
				return true;
			case 4406: // saradomin leave
			case 4407: // zamorak leave
				if (Boundary.inside(player, SAFE_AREAS)) {
					leave(player, false);
				}
				return true;
			case 4458:
				if (Boundary.inside(player, SAFE_AREAS)) {
					player.performAnimation(LOOT);
					player.getInventory().add(BANDAGE);
					return true;
				}
				return true;
			case 4461: // barricades
				player.getInventory().add(BARRICADE);
				return true;
			case 4463: // explosive potion
				player.getInventory().add(EXPLOSIVE_POTION);
				return true;
			case 4464: // pickaxe table
				player.getInventory().add(BRONZE_PICK_AXE);
				return true;
			case 4459: // tinderbox
				player.getInventory().add(TINDERBOX);
				return true;
			case 4462: // rope
				player.getInventory().add(ROPE);
				return true;
			case 4460: // rocks
				player.getInventory().add(ROCK);
				return true;

			}
		}
		return false;
	}
}