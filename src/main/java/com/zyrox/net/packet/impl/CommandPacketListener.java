package com.zyrox.net.packet.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang.StringUtils;

import com.google.common.primitives.Ints;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.zyrox.GameSettings;
import com.zyrox.commands.CommandHandler;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.engine.task.impl.PlayerDeathTask;
import com.zyrox.model.Animation;
import com.zyrox.model.Flag;
import com.zyrox.model.GameMode;
import com.zyrox.model.GameObject;
import com.zyrox.model.Graphic;
import com.zyrox.model.Item;
import com.zyrox.model.Locations;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.PlayerRights;
import com.zyrox.model.Position;
import com.zyrox.model.Skill;
import com.zyrox.model.container.impl.Inventory;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.model.definitions.NPCDrops;
import com.zyrox.model.definitions.NpcDefinition;
import com.zyrox.model.input.impl.SetTitle;
import com.zyrox.model.log.impl.CommandLog;
import com.zyrox.net.packet.Packet;
import com.zyrox.net.packet.PacketListener;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.net.security.ConnectionHandler;
import com.zyrox.util.Misc;
import com.zyrox.util.NameUtils;
import com.zyrox.util.TreasureIslandLootDumper;
import com.zyrox.world.World;
import com.zyrox.world.content.Achievements;
import com.zyrox.world.content.Achievements.AchievementData;
import com.zyrox.world.content.CustomObjects;
import com.zyrox.world.content.Debug;
import com.zyrox.world.content.EquipHandler;
import com.zyrox.world.content.EvilTrees;
import com.zyrox.world.content.PlayerLogs;
import com.zyrox.world.content.PlayerPanel;
import com.zyrox.world.content.ShootingStar;
import com.zyrox.world.content.StaffList;
import com.zyrox.world.content.clan.ClanChatManager;
import com.zyrox.world.content.combat.strategy.CombatStrategies;
import com.zyrox.world.content.combat.strategy.impl.CorporealBeast;
import com.zyrox.world.content.combat.strategy.zulrah.ZulrahConstants;
import com.zyrox.world.content.combat.weapon.CombatSpecial;
import com.zyrox.world.content.droppreview.SLASHBASH;
import com.zyrox.world.content.freeforall.FreeForAll;
import com.zyrox.world.content.greatolm.GreatOlm;
import com.zyrox.world.content.greatolm.RaidsParty;
import com.zyrox.world.content.greatolm.RaidsReward;
import com.zyrox.world.content.skill.SkillManager;
import com.zyrox.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.zyrox.world.content.skill.impl.herblore.Decanting;
import com.zyrox.world.content.teleportation.TeleportData;
import com.zyrox.world.content.teleportation.TeleportInterface;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.content.transportation.TeleportType;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;
import com.zyrox.world.entity.impl.player.PlayerHandler;
import com.zyrox.world.entity.impl.player.link.rights.StaffPrivilegeLevel;

/**
 * This packet listener manages commands a player uses by using the command
 * console prompted by using the "`" char.
 *
 * @author Gabriel Hannason
 */

public class CommandPacketListener implements PacketListener {

	private static final String COMMAND_LOCATION = Command.class.getPackage().getName() + ".impl";

	private static final Map<String, Command> COMMANDS = new HashMap<>();

	public static Map<String, Command> getCommands() {
		return COMMANDS;
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		long start = System.currentTimeMillis();

		String playerCommand = Misc.readString(packet.getBuffer());

		if (player.requiresUnlocking()) {
			return;
		}

		String[] args = playerCommand.split(" ");

		if (playerCommand.contains("\r") || playerCommand.contains("\n")) {
			return;
		}

		Debug.write(player.getName(), "CommandPacketListener", new String[] { "playerCommand: " + playerCommand, });

		String commandName = args[0].toLowerCase();

		Command command = COMMANDS.get(commandName);

		int privilegeLevel = player.getRights().getPrivilegeLevel();
		int donatorPrivilegeLevel = PlayerRights.getPrivilegeLevelForAmountDonated(player);
		if (command != null) {

			if (command instanceof NameCommand) {
				NameCommand nameCommand = (NameCommand) command;

				if (!nameCommand.getValidNames().contains(player.getUsername())) {
					player.sendMessage("You do not have @dre@permission</col> to use this command.");
					return;
				}
			} else {
				boolean meetsStaffRequirement = privilegeLevel >= command.getMininumStaffPrivilege();
				boolean meetsDonatorRequirements = (donatorPrivilegeLevel >= command.getMinimumDonatorPrivilege())
						|| PlayerRights.getPrivilegeLevelForAmountDonated(player) >= command
								.getMinimumDonatorPrivilege();
				if (!meetsStaffRequirement) {
					player.sendMessage("You do not have @dre@permission</col> to use this command.");
					return;
				} else if (!meetsDonatorRequirements) {
					player.sendMessage("The minimum rights required for the '@dre@" + commandName.toLowerCase()
							+ "</col>' command is: " + Misc.formatPlayerName(
									PlayerRights.forDonatorPrivilege(command.getMinimumDonatorPrivilege()).name()));
					return;
				}
			}

			try {
				if (command.execute(player,
						args.length == 1 ? new String[0] : Arrays.copyOfRange(args, 1, args.length))) {
					new CommandLog(player.getName(), playerCommand, Misc.getTime()).submit();
				}
			} catch (IllegalArgumentException e) {
				player.sendMessage("Usage @dre@-> </col>" + StringUtils.join(command.getUsage(), ", "));
			} catch (Exception e) {
				player.sendMessage("There was an error whilst trying to execute the command...");
				e.printStackTrace();
			}

			return;
		}

		if (CommandHandler.processed(args[0], player, playerCommand)) {
			new CommandLog(player.getName(), playerCommand, Misc.getTime()).submit();
			return;
		}

		try {

			playerCommands(player, args, playerCommand);

			if (privilegeLevel >= StaffPrivilegeLevel.SUPPORT) {
				supportCommands(player, args, playerCommand);
			}

			if (privilegeLevel >= StaffPrivilegeLevel.MODERATOR) {
				moderatorCommands(player, args, playerCommand);
			}

			if (privilegeLevel >= StaffPrivilegeLevel.HEAD_MODERATOR) {
				administratorCommands(player, args, playerCommand);
			}

			if (privilegeLevel >= StaffPrivilegeLevel.MANAGER) {
				ownerCommands(player, args, playerCommand);
				developerCommands(player, args, playerCommand);
			}

			if (PlayerRights.DONATOR.hasEnoughDonated(player)) {
				memberCommands(player, args, playerCommand);
			}

			new CommandLog(player.getName(), playerCommand, Misc.getTime()).submit();
		} catch (Exception exception) {
			player.getPacketSender().sendMessage("Error executing that command.");
			exception.printStackTrace();
		}

		long end = System.currentTimeMillis();

		long cycle = end - start;

		if (cycle >= 500) {
			System.err.println(cycle + "ms - command packet- " + playerCommand + " - " + player.getRights().name());
		}
	}

	public static void init() {
		ClassPath cp = null;
		try {
			cp = ClassPath.from(Command.class.getClassLoader());
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (ClassInfo ci : cp.getTopLevelClasses(COMMAND_LOCATION)) {
			Command command;
			try {
				command = ((Command) Class.forName(ci.getName()).newInstance());
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
				continue;
			}

			CommandHeader header = command.getClass().getAnnotation(CommandHeader.class);

			if (header == null) {
				System.out.println("Missing command header for " + ci.getSimpleName());
				continue;
			}

			for (String name : header.command()) {
				COMMANDS.put(name, command);
			}
		}
		if (COMMANDS.size() == 0) {
			File commandsDirectory = new File("target/classes/" + COMMAND_LOCATION.replaceAll("\\.", "\\\\"));
			for (File commandFile : commandsDirectory.listFiles()) {
				Command command;
				try {
					command = ((Command) Class
							.forName(COMMAND_LOCATION + "." + commandFile.getName().replaceAll("\\.class", ""))
							.newInstance());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					continue;
				}

				CommandHeader header = command.getClass().getAnnotation(CommandHeader.class);

				if (header == null) {
					System.out.println("Missing command header for " + commandFile.getName());
					continue;
				}

				for (String name : header.command()) {
					COMMANDS.put(name, command);
				}
			}
		}
		System.out.println("Loaded " + getCommands().size() + " commands.");
	}

	private static void playerCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("wellbenefits") || command[0].equals("well")) {
			World.getWell().displayStatus(player);
			return;
		}
		if (command[0].equalsIgnoreCase("testsite")) {
			player.getPA().sendUrl("https://zyrox.org/"); //use this and replace site whenever u wanna send a site
			return;
		}
		if (command[0].equalsIgnoreCase("fkeys")) {
			player.getPacketSender().sendInterface(28630);
			return;
		}
		if (command[0].equalsIgnoreCase("equip")) {
			EquipHandler.open(player);
			return;
		}
		
		if (command[0].equalsIgnoreCase("open")) {
			//Bosses Teleporter
			player.getTeleportInterface().open(TeleportData.ZULRAH);
			player.getTeleportInterface().open(TeleportData.VORKATH);
			player.getTeleportInterface().open(TeleportData.TEKTON);
			player.getTeleportInterface().open(TeleportData.SKOTIZO);
			player.getTeleportInterface().open(TeleportData.TARN);
			player.getTeleportInterface().open(TeleportData.SLASH);
			player.getTeleportInterface().open(TeleportData.HYDRA);
			player.getTeleportInterface().open(TeleportData.KARKEN);
			player.getTeleportInterface().open(TeleportData.GODWARS);
			player.getTeleportInterface().open(TeleportData.BORK);
			player.getTeleportInterface().open(TeleportData.BARREL);
			player.getTeleportInterface().open(TeleportData.BAVATAR);
			player.getTeleportInterface().open(TeleportData.CORP);

			//Monsters Teleporter
			player.getTeleportInterface().open(TeleportData.ROCK);
			player.getTeleportInterface().open(TeleportData.YAKS);
			player.getTeleportInterface().open(TeleportData.COWS);
			player.getTeleportInterface().open(TeleportData.CHICKEN);

			//Skilling Teleporter

			//Donator Teleporter
			player.getTeleportInterface().open(TeleportData.REGULARDONATOR);
			player.getTeleportInterface().open(TeleportData.SUPERDONATOR);
			player.getTeleportInterface().open(TeleportData.EXTRAMEDONATOR);
			return;
		}

		if (command[0].equals("find")) {
			String name = wholeCommand.substring(5).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendMessage("Found item with name ["
							+ ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
			}
		}

		if (command[0].equalsIgnoreCase("ffaleave")) {
			if (player.getLocation() != Location.FFALOBBY || player.getLocation() != Location.FFALOBBY) {
				player.getPA().sendMessage("You can only use this in the ffa arenas");
				return;
			}

			if (player.getLocation() == Location.DUNGEONEERING) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (player.getLocation() == Location.IN_JAIL) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (player.getLocation() == Location.DUEL_ARENA) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			player.getPA().sendInterfaceRemoval();
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				public void execute() {
					if (tick == 0) {

					} else if (tick >= 3) {
						FreeForAll.leaveArena(player);
						this.stop();
					}
					tick++;
				}
			});
		}

		if (command[0].equalsIgnoreCase("ffastaffleave")) {
			if (player.getLocation() != Location.FFALOBBY || player.getLocation() != Location.FFALOBBY) {
				player.getPA().sendMessage("You can only use this in the ffa arenas");
				return;
			}

			if (player.getLocation() == Location.DUNGEONEERING) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (player.getLocation() == Location.IN_JAIL) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (player.getLocation() == Location.DUEL_ARENA) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			player.getPA().sendInterfaceRemoval();
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				public void execute() {
					if (tick == 0) {

					} else if (tick >= 3) {
						FreeForAll.staffLeaveArena(player);
						this.stop();
					}
					tick++;
				}
			});
			return;
		}

		if (command[0].equalsIgnoreCase("ffa")) {
			if (player.getLocation() == Location.DUNGEONEERING) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (FreeForAll.lobbyOpened == true) {
				FreeForAll.initiateLobby(player);
			} else {
				player.getPacketSender().sendMessage("No active ffa event");
			}
			return;
		}

		/*
		 * if (command[0].equalsIgnoreCase("2fa")) {
		 * player.getTwoFactorAuth().showOptions(); }
		 */

		if (command[0].equalsIgnoreCase("kraken")) {
			player.getPacketSender().sendMessage("Teleporting you to kraken.");
			player.getKraken().enter(player, true);
			return;
		}

		if (command[0].equalsIgnoreCase("zulrah")) {
			if (Locations.inZulrah(player)) {
				if (player.lastZulrah != null && player.lastZulrah.isRegistered()) {
					player.sendMessage("You must kill Zulrah before using this command again.");
					return;
				}
				ZulrahConstants.startBossFight(player);
				return;
			}
			TeleportHandler.teleportZulrah(player);
			return;
		}

		if (command[0].equalsIgnoreCase("grabregion")) {
			int regionX = player.getPosition().getX() >> 3;
			int regionY = player.getPosition().getY() >> 3;
			int regionId = ((regionX / 8) << 8) + (regionY / 8);
			player.getPacketSender().sendMessage("Region id: " + regionId);
			return;
		}

		if (command[0].equals("staff")) {
			StaffList.showStaff(player);
			return;
		}

		if (command[0].equalsIgnoreCase("demonics")) {
			TeleportHandler.teleportPlayer(player, new Position(2128, 5647, 0),
					player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equals("tree")) {
			if (EvilTrees.SPAWNED_TREE == null) {
				player.getPacketSender()
						.sendMessage("<img=483> <shad=1><col=FF9933> The Evil Tree has not sprouted yet!");
			} else {
				player.getPacketSender().sendMessage("<img=483> <shad=1><col=FF9933> The Evil Tree has sprouted at: "
						+ EvilTrees.SPAWNED_TREE.getTreeLocation().playerPanelFrame + "");
			}
			return;
		}

		if (command[0].equals("star")) {
			if (ShootingStar.CRASHED_STAR == null) {
				player.getPacketSender().sendMessage("<img=731> <shad=E9E919>The Shooting Star has not fallen yet!");
			} else {
				player.getPacketSender().sendMessage("<img=731> <shad=E9E919>The Shooting Star has spawned at: "
						+ ShootingStar.CRASHED_STAR.getStarLocation().playerPanelFrame + "");
			}
			return;
		}

		if (command[0].equals("decant")) {
			// PotionDecanting.decantPotions(player);
			Decanting.startDecanting(player);
			return;
		}

		if (command[0].equalsIgnoreCase("setemail")) {
			String email = wholeCommand.substring(9);
			player.setEmailAddress(email);
			player.getPacketSender().sendMessage("You set your account's email adress to: [" + email + "] ");
			Achievements.finishAchievement(player, AchievementData.SET_AN_EMAIL_ADDRESS);
			PlayerPanel.refreshPanel(player);
			return;
		}

		if (command[0].equalsIgnoreCase("dropparty")) {
			TeleportHandler.teleportPlayer(player, new Position(2736, 3475), player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equalsIgnoreCase("removetitle")) {
			player.setTitle("");
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			return;
		}

		if (command[0].equalsIgnoreCase("pc")) {
			TeleportHandler.teleportPlayer(player, new Position(2663, 2654), player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equalsIgnoreCase("raids")) {
			TeleportHandler.teleportPlayer(player, new Position(1230, 3558), player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equalsIgnoreCase("barrows")) {
			TeleportHandler.teleportPlayer(player, new Position(3565, 3313), player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equalsIgnoreCase("duel")) {
			TeleportHandler.teleportPlayer(player, new Position(3364, 3266), player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equalsIgnoreCase("mole")) {
			TeleportHandler.teleportPlayer(player, new Position(1761, 5181), player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equalsIgnoreCase("warmonger")) {
			TeleportHandler.teleportPlayer(player, new Position(3105, 4378), player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equalsIgnoreCase("tekton")) {
			TeleportHandler.teleportPlayer(player, new Position(3053, 5210), player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equalsIgnoreCase("bork")) {
			TeleportHandler.teleportPlayer(player, new Position(3102, 5536), player.getSpellbook().getTeleportType());
			return;
		}
		if (command[0].equalsIgnoreCase("nex")) {
			TeleportHandler.teleportPlayer(player, new Position(2903, 5204), player.getSpellbook().getTeleportType());
			return;
		}
		if (command[0].equalsIgnoreCase("lava")) {
			TeleportHandler.teleportPlayer(player, new Position(2455, 5213), player.getSpellbook().getTeleportType());
			return;
		}
		if (command[0].equalsIgnoreCase("hydra")) {
			TeleportHandler.teleportPlayer(player, new Position(1351, 10258), player.getSpellbook().getTeleportType());
			return;
		}
		if (command[0].equalsIgnoreCase("jatizso")) {
			TeleportHandler.teleportPlayer(player, new Position(2416, 3826), player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equalsIgnoreCase("jati")) {
			TeleportHandler.teleportPlayer(player, new Position(2416, 3826), player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equalsIgnoreCase("jatiz")) {
			TeleportHandler.teleportPlayer(player, new Position(2416, 3826), player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equalsIgnoreCase("wyvern")) {
			TeleportHandler.teleportPlayer(player, new Position(1667, 5676, 0),
					player.getSpellbook().getTeleportType());
			return;
		}
		if (command[0].equalsIgnoreCase("varrock")) {
			TeleportHandler.teleportPlayer(player, new Position(3213, 3424), player.getSpellbook().getTeleportType());
			return;
		}
		if (command[0].equalsIgnoreCase("daily")) {
			player.getDailyLogin().openInterface(player);
			return;
		}

		if (command[0].equalsIgnoreCase("ardy")) {
			TeleportHandler.teleportPlayer(player, new Position(2661, 3306), player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equalsIgnoreCase("falador")) {
			TeleportHandler.teleportPlayer(player, new Position(2963, 3381), player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equalsIgnoreCase("lumby")) {
			TeleportHandler.teleportPlayer(player, new Position(3223, 3218), player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equalsIgnoreCase("corp")) {
			CorporealBeast.getDialogue(player);
			return;
		}

		if (wholeCommand.equalsIgnoreCase("forums") || wholeCommand.equalsIgnoreCase("forum")) {
			player.getPacketSender().sendString(1, "https://Zyrox.org/forums/");
			player.getPacketSender().sendMessage("Attempting to open: Zyrox.org/forums");
			return;
		}

		if (wholeCommand.equalsIgnoreCase("facebook") || wholeCommand.equalsIgnoreCase("fb")) {
			player.getPacketSender().sendString(1, "www.facebook.com/Zyrox");
			player.getPacketSender().sendMessage("Attempting to open: www.facebook.com/Zyrox");
			return;
		}

		if (wholeCommand.equalsIgnoreCase("hiscores") || wholeCommand.equalsIgnoreCase("hs")) {
			player.getPacketSender().sendString(1, "http://Zyrox.org/hiscores");
			player.getPacketSender().sendMessage("Attempting to open: Hiscores page");
			return;
		}

		if (command[0].equals("save")) {
			player.save();
			player.getPacketSender().sendMessage("Your progress has been saved.");
			return;
		}

		if (command[0].equalsIgnoreCase("[cn]")) {
			if (player.getInterfaceId() == 40172) {
				ClanChatManager.setName(player, wholeCommand.substring(wholeCommand.indexOf(command[1])));
			}
			return;
		}

	}

	private static void memberCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("title")) {
			player.setInputHandling(new SetTitle());
			player.getPacketSender().sendEnterInputPrompt("Enter the title you would like to set");
		}

		if (command[0].equalsIgnoreCase("dzone") || command[0].equalsIgnoreCase("dz")) {
			TeleportHandler.teleportPlayer(player, new Position(2066, 3275), player.getSpellbook().getTeleportType());
			return;
		}

		if (command[0].equalsIgnoreCase("bank")) {

			if (player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.FIGHT_PITS
					|| player.getLocation() == Location.FIGHT_CAVES || player.getLocation() == Location.DUEL_ARENA
					|| player.getLocation() == Location.RECIPE_FOR_DISASTER || player.getLocation() == Location.INFERNO
					|| player.getLocation() == Location.IN_JAIL || player.getLocation() == Location.CASTLE_WARS_GAME
					|| player.getLocation() == Location.WILDERNESS
					|| player.getLocation() == Location.CASTLE_WARS_WAITING_LOBBY
					|| player.getLocation() == Location.COWS) {
				player.getPacketSender().sendMessage("You can not open your bank here!");
				return;
			}

			if (!PlayerRights.EXECUTIVE_DONATOR.hasEnoughDonated(player)) {
				player.getPacketSender().sendMessage("You must be Executive to do this!");
				return;
			}

			if (player.getLocation() != Location.DUNGEONEERING) {
				if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {

					player.getPacketSender().sendInterfaceRemoval()
							.sendMessage("Your game mode restricts you from doing this action.");
					return;
				}
			}

			player.setTempBankTabs(null);
			player.getBank(player.getCurrentBankTab()).open();
		}

	}

	private static void supportCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("kick")) {
			String player2 = wholeCommand.substring(5);
			Player playerToKick = World.getPlayerByName(player2);
			if (playerToKick == null) {
				player.getPacketSender().sendConsoleMessage("Player " + player2 + " couldn't be found on Zyrox.");
				return;
			} else if (player.getRights().isHighStaff() || playerToKick.getLocation() != Location.WILDERNESS) {
				World.deregister(playerToKick);
				PlayerHandler.handleLogout(playerToKick);
				player.getPacketSender().sendConsoleMessage("Kicked " + playerToKick.getUsername() + ".");
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " just kicked " + playerToKick.getUsername() + "!");
			}
		}

		if (command[0].equalsIgnoreCase("bank")) {
			if (player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.WILDERNESS
					|| player.getLocation() == Location.FIGHT_PITS || player.getLocation() == Location.FIGHT_CAVES
					|| player.getLocation() == Location.DUEL_ARENA || player.getLocation() == Location.INFERNO
					|| player.getLocation() == Location.IN_JAIL || player.getLocation() == Location.CASTLE_WARS_GAME
					|| player.getLocation() == Location.RECIPE_FOR_DISASTER || player.getLocation() == Location.FFALOBBY
					|| player.getLocation() == Location.FREE_FOR_ALL_ARENA
					|| player.getLocation() == Location.FREE_FOR_ALL_WAIT || player.getLocation() == Location.FFAARENA
					|| player.getLocation() == Location.FIGHT_PITS_WAIT_ROOM
					|| player.getLocation() == Location.CASTLE_WARS_WAITING_LOBBY
					|| player.getLocation() == Location.COWS) {
				player.getPacketSender().sendMessage("You can not open your bank here!");
				return;
			}
			player.setTempBankTabs(null);
			player.getBank(player.getCurrentBankTab()).open();
			return;
		}

		if (command[0].equalsIgnoreCase("saveall")) {
			World.savePlayers();
			player.getPacketSender().sendMessage("Saved players!");
		}

		if (command[0].equalsIgnoreCase("movehome")) {
			String player2 = command[1];
			player2 = Misc.formatText(player2.replaceAll("_", " "));
			if (command.length >= 3 && command[2] != null) {
				player2 += " " + Misc.formatText(command[2].replaceAll("_", " "));
			}
			Player playerToMove = World.getPlayerByName(player2);

			if (playerToMove != null) {
				if (playerToMove.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.FIGHT_PITS
						|| player.getLocation() == Location.FIGHT_CAVES || player.getLocation() == Location.DUEL_ARENA
						|| player.getLocation() == Location.RECIPE_FOR_DISASTER
						|| player.getLocation() == Location.FFALOBBY
						|| player.getLocation() == Location.FREE_FOR_ALL_ARENA
						|| player.getLocation() == Location.FREE_FOR_ALL_WAIT
						|| player.getLocation() == Location.FFAARENA
						|| player.getLocation() == Location.FIGHT_PITS_WAIT_ROOM) {
					player.getPacketSender().sendMessage("User is in a minigame unavailable at moment!");
					return;
				}

				playerToMove.moveTo(GameSettings.DEFAULT_POSITION.copy());
				playerToMove.getPacketSender()
						.sendMessage("You've been teleported home by " + player.getUsername() + ".");
				player.getPacketSender()
						.sendConsoleMessage("Sucessfully moved " + playerToMove.getUsername() + " to home.");
			}
		}
	}

	private static void moderatorCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("toggleinvis")) {
			player.setNpcTransformationId(player.getNpcTransformationId() > 0 ? -1 : 8254);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}

		if (command[0].equalsIgnoreCase("kick")) {
			String player2 = wholeCommand.substring(5);
			Player playerToKick = World.getPlayerByName(player2);
			if (playerToKick == null) {
				player.getPacketSender().sendConsoleMessage("Player " + player2 + " couldn't be found on Zyrox.");
				return;
			} else if (playerToKick.getLocation() != Location.WILDERNESS) {
				World.deregister(playerToKick);
				PlayerHandler.handleLogout(playerToKick);
				player.getPacketSender().sendConsoleMessage("Kicked " + playerToKick.getUsername() + ".");
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " just kicked " + playerToKick.getUsername() + "!");
			}
		}
	}

	private static void administratorCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("kill-player")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(5));
			TaskManager.submit(new PlayerDeathTask(player2, true));
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " just ::killed " + player2.getUsername() + "!");
			player.getPacketSender().sendMessage("Killed player: " + player2.getUsername() + "");
			player2.getPacketSender().sendMessage("You have been Killed by " + player.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("kills")) {
			player.getPacketSender()
					.sendMessage("total kills: " + player.getPlayerKillingAttributes().getPlayerKills());
		}
		if (command[0].equalsIgnoreCase("mma")) {
			TeleportHandler.teleportPlayer(player, new Position(2038, 4497), TeleportType.NORMAL);

		}
		if (command[0].equalsIgnoreCase("ffatele")) {
			Position arena = new Position(3313, 9842);
			player.moveTo(arena);
		}
		if (command[0].equalsIgnoreCase("ffakill")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(5));
			if (player2 == null) {
				player.sendMessage("Player not found.");
				return;
			}
			TaskManager.submit(new PlayerDeathTask(player2));
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " just ::killed " + player2.getUsername() + "!");
			player.getPacketSender().sendMessage("Killed player: " + player2.getUsername() + "");
			player2.getPacketSender().sendMessage("You have been Killed by " + player.getUsername() + ".");
		}

		if (command[0].equalsIgnoreCase("ffaevent")) {
			Integer id = null;
			Integer amount = null;
			if (command.length == 2) {
				id = Ints.tryParse(command[1]);
			} else if (command.length == 3) {
				amount = Ints.tryParse(command[2]);
			}
			FreeForAll.initiateEvent(player, id, amount);
		}

		if (command[0].equalsIgnoreCase("ffastart")) {
			FreeForAll.openPortal(player);
		}
		if (command[0].equalsIgnoreCase("ffaclose")) {
			FreeForAll.closePortal(player);
		}

		if (command[0].equalsIgnoreCase("gobject")) {
			int id = Integer.parseInt(command[1]);

			player.getPacketSender().sendConsoleMessage("Sending object: " + id);

			GameObject objid = new GameObject(id, player.getPosition(), 10, 0);
			CustomObjects.spawnGlobalObject(objid);
		}

		if (command[0].equalsIgnoreCase("pouch")) {
			Player target = PlayerHandler.getPlayerForName(wholeCommand.substring(6));
			long gold = target.getMoneyInPouch();
			player.getPacketSender()
					.sendMessage("Player has: " + Misc.insertCommasToNumber(String.valueOf(gold)) + " coins in pouch");

		}
		if (command[0].equalsIgnoreCase("getpin")) {

			String name = wholeCommand.substring(command[0].length() + 1);

			if (name.length() > 0) {

				new Thread(new Runnable() {

					@Override
					public void run() {

						Player other = Misc.accessPlayer(name);

						if (other == null) {
							player.sendMessage("That player could not be found.");
							return;
						}

						player.sendMessage("The bank pin for " + other.getUsername() + " is: "
								+ other.getBankPinAttributes().getBankPin()[0] + " , "
								+ other.getBankPinAttributes().getBankPin()[1] + " , "
								+ other.getBankPinAttributes().getBankPin()[2] + " , "
								+ other.getBankPinAttributes().getBankPin()[3]);

					}

				}).start();

			} else {
				player.sendMessage("Please, enter a valid username to fetch a password for.");
			}

		}

		if (command[0].equals("checkinv")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(9));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}
			if (!player.getRights().isHighStaff() && player2.getRights().isHighStaff()) {
				player.getPacketSender().sendConsoleMessage("Access denied.");
				return;
			}
			Inventory inventory = new Inventory(player);
			inventory.resetItems();
			inventory.setItems(player2.getInventory().getCopiedItems());
			player.getPacketSender().sendItemContainer(inventory, 3823);
			player.getPacketSender().sendInterface(3822);
		}

		if (wholeCommand.toLowerCase().startsWith("yell") && player.getRights() == PlayerRights.PLAYER) {
			player.getPacketSender().sendMessage("Only members can yell. To become one, simply use ::store, donate $20")
					.sendMessage("and then claim it.");
		}

		if (command[0].equals("gold")) {
			Player p = World.getPlayerByName(wholeCommand.substring(5));
			if (p != null) {
				long gold = 0;
				for (Item item : p.getInventory().getItems()) {
					if (item != null && item.getId() > 0 && item.tradeable(p)) {
						gold += item.getDefinition().getValue();
					}
				}
				for (Item item : p.getEquipment().getItems()) {
					if (item != null && item.getId() > 0 && item.tradeable(p)) {
						gold += item.getDefinition().getValue();
					}
				}
				for (int i = 0; i < 9; i++) {
					for (Item item : p.getBank(i).getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable(p)) {
							gold += item.getDefinition().getValue();
						}
					}
				}
				gold += p.getMoneyInPouch();
				player.getPacketSender().sendMessage(
						p.getUsername() + " has " + Misc.insertCommasToNumber(String.valueOf(gold)) + " coins.");
			} else {
				player.getPacketSender().sendMessage("Can not find player online.");
			}
		}

		if (command[0].equals("cashineco")) {
			int gold = 0, plrLoops = 0;
			for (Player p : World.getPlayers()) {
				if (p != null) {
					for (Item item : p.getInventory().getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable(p)) {
							gold += item.getDefinition().getValue();
						}
					}
					for (Item item : p.getEquipment().getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable(p)) {
							gold += item.getDefinition().getValue();
						}
					}
					for (int i = 0; i < 9; i++) {
						for (Item item : player.getBank(i).getItems()) {
							if (item != null && item.getId() > 0 && item.tradeable(player)) {
								gold += item.getDefinition().getValue();
							}
						}
					}
					gold += p.getMoneyInPouch();
					plrLoops++;
				}
			}
			player.getPacketSender().sendMessage(
					"Total gold in economy right now: " + gold + ", went through " + plrLoops + " players items.");
		}

		if (command[0].equals("getid")) {
			String name = wholeCommand.substring(3).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendConsoleMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = ItemDefinition.getMaxAmountOfItems() - 1; i > 0; i--) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendConsoleMessage("Found item with name ["
							+ ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
			}
		}

	}

	private static void ownerCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("coords") || command[0].equalsIgnoreCase("mypos")) {
			player.sendMessage(player.getPosition().toString());
		}
		if (command[0].equalsIgnoreCase("positiondetails")) {
			player.sendMessage(
					"[ <col=D9D919>Position</col> ] Current: " + player.getPosition().toStringFormated() + ".");
			player.sendMessage("[ <col=D9D919>Region</col> ] Current: " + player.getPosition().getRegionID() + " "
					+ new Position(player.getPosition().getRegionX(), player.getPosition().getRegionY())
							.toStringFormated()
					+ ".");

		}
		if (wholeCommand.equals("afk")) {
			World.sendMessage("<img=10> <col=E9E919><shad=0>" + player.getUsername()
					+ ": I am now away, please don't message me; I won't reply.");
		}

		if (command[0].equals("force-tree")) {
			EvilTrees.timer.setTime(0);
		}
		if (command[0].equals("setlevel")) {
			int skillId = Integer.parseInt(command[1]);
			int level = Integer.parseInt(command[2]);
			if (level > 1500000) {
				player.getPacketSender().sendMessage("You can only have a maxmium level of 150000.");
				return;
			}
			Skill skill = Skill.forId(skillId);
			player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
					SkillManager.getExperienceForLevel(skill, level));
			player.getPacketSender().sendMessage("You have set your " + skill.getName() + " level to " + level);
		}

		if (command[0].equals("staff-only")) {
			GameSettings.STAFF_ONLY = !GameSettings.STAFF_ONLY;
			player.sendMessage("Staff only : " + GameSettings.STAFF_ONLY);
		}

		if (wholeCommand.equals("spec")) {
			player.setSpecialPercentage(100);
			CombatSpecial.updateBar(player);
			player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER),
					true);
			player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
					player.getSkillManager().getMaxLevel(Skill.CONSTITUTION), true);
			player.getPacketSender().sendMessage("Your special attack, prayer, and health has been restored.");
		}
	}

	private static void developerCommands(Player player, String command[], String wholeCommand) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("./devcommands.txt", true));
			out.write(new Date().toString() + " - " + player.getName() + " - rank:" + player.getRights().name() + "-"
					+ command[0] + " -" + wholeCommand);
			out.newLine();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (command[0].equalsIgnoreCase("npcspawned")) {
			player.sendMessage("There are currently " + World.getNpcs().size() + " spawned and there are "
					+ World.getNpcs().spaceLeft() + "/" + World.getNpcs().capacity() + " slots left.");
			return;
		}
		if (command[0].equalsIgnoreCase("item")) {
			int id = Integer.parseInt(command[1]);
			if(id > ItemDefinition.getMaxAmountOfItems() || ItemDefinition.forId(id) == null) {
				player.sendMessage("This item does not exist or is higher than " + ItemDefinition.getMaxAmountOfItems());
				return;
			}
			long amount = command.length == 2 ? 1 : Long.parseLong(command[2].trim().toLowerCase().replaceAll("k", "000").replaceAll("m", "000000").replaceAll("b", "000000000"));
			if (amount > Integer.MAX_VALUE && id != 995) {
					amount = Integer.MAX_VALUE;
			}
			Item item = new Item(id);
			if(!item.getDefinition().isStackable()) {
				if(amount > player.getInventory().getFreeSlots()) {
					amount = player.getInventory().getFreeSlots();
				}
			}
			if(id == 995 && amount >= Integer.MAX_VALUE) {
				player.setMoneyInPouch(player.getMoneyInPouch() + amount);
				player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
			} else {
				item = new Item(id, (int) amount);
				player.getInventory().add(item, true);
			}
		}
		if (command[0].equals("setlevel")) {
			int skillId = Integer.parseInt(command[1]);
			int level = Integer.parseInt(command[2]);
			if (level > 1500000) {
				player.getPacketSender().sendMessage("You can only have a maxmium level of 150000.");
				return;
			}
			Skill skill = Skill.forId(skillId);
			player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,SkillManager.getExperienceForLevel(skill, level));
			player.getPacketSender().sendMessage("You have set your " + skill.getName() + " level to " + level);
		}

		if (command[0].equalsIgnoreCase("location")) {
			String loc = player.getLocation().name();
			player.getPacketSender().sendMessage("Location: " + loc);
		}

		if (command[0].equalsIgnoreCase("emptypouch2")) {
			player.setMoneyInPouch(0);
			player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch() + "");
		}

		if (command[0].equalsIgnoreCase("give99a")) {
			String name = wholeCommand.substring(8);
			Player target = World.getPlayerByName(name);
			Achievements.finishAchievement(target, AchievementData.REACH_LEVEL_99_IN_ALL_SKILLS);

		}

		if (command[0].toLowerCase().equalsIgnoreCase("olm")) {
			new RaidsParty(player).create();
			GreatOlm.start(player);
		}

		if (command[0].equalsIgnoreCase("rr")) {
			RaidsReward.grantLoot(player.getMinigameAttributes().getRaidsAttributes().getParty());

			player.getPacketSender().sendItemOnInterface(85077, player.getRaidsLoot().getId(),
					player.getRaidsLoot().getAmount());
			player.getPacketSender().sendItemOnInterface(85078, player.getRaidsLootSecond().getId(),
					player.getRaidsLoot().getAmount());
			player.getPacketSender().sendInterface(85075);
			player.getPacketSender().sendInterface(85075);
		}

		if (command[0].toLowerCase().equalsIgnoreCase("create")) {
			new RaidsParty(player).create();
		}

		if (command[0].equalsIgnoreCase("title")) {
			String title = wholeCommand.substring(6);

			if (title == null || title.length() <= 2 || title.length() > 9 || !NameUtils.isValidName(title)) {
				player.getPacketSender().sendMessage("You can not set your title to that!");
				return;
			}
			player.setTitle("@or2@" + title);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equalsIgnoreCase("sstar")) {
			CustomObjects.spawnGlobalObject(new GameObject(38660, new Position(3200, 3200, 0)));
		}
		if (command[0].equals("sendstring")) {
			int child = Integer.parseInt(command[1]);
			String string = command[2];
			player.getPacketSender().sendString(child, string);
		}
		if (command[0].equalsIgnoreCase("kbd")) {
			SLASHBASH.startPreview(player);

		}

		if (command[0].equalsIgnoreCase("multiloc")) {
			Location.inMulti(player);
			player.getPA().sendMessage("" + Location.inMulti(player));
		}

		if (command[0].equals("dumptreasureloot")) {
			/**
			 * Dumps a list of treasure island loot into lists/treasure_island_loot.txt
			 */
			TreasureIslandLootDumper.dump();
			player.getPacketSender()
					.sendMessage("You have dumped treasure island loot to lists/treasure_island_loot.txt");
		}
		if (command[0].equalsIgnoreCase("bank")) {
			player.setTempBankTabs(null);
			player.getBank(player.getCurrentBankTab()).open();
		}

		if (command[0].equals("tasks")) {
			player.getPacketSender().sendConsoleMessage("Found " + TaskManager.getTaskAmount() + " tasks.");
		}
		if (command[0].equalsIgnoreCase("reloadcpubans")) {
			ConnectionHandler.reloadUUIDBans();
			player.getPacketSender().sendConsoleMessage("UUID bans reloaded!");
			return;
		}
		if (command[0].equals("reloadnpcs")) {
			NpcDefinition.parseNpcs().load();
			World.sendMessage("@red@NPC Definitions Reloaded.");
		}

		if (command[0].equals("reloaddrops")) {
			NPCDrops.getDrops().clear();
			NPCDrops.parseDrops();
			World.sendMessage("Npc drops reloaded");
		}

		if (command[0].equals("reloadcombat")) {
			CombatStrategies.init();
			World.sendMessage("@red@Combat Strategies have been reloaded");
		}

		if (command[0].equalsIgnoreCase("reloaditems")) {
			ItemDefinition.init();
			World.sendMessage("@red@Items have been reloaded");
		}

		if (command[0].equals("memory")) {
			// ManagementFactory.getMemoryMXBean().gc();
			/*
			 * MemoryUsage heapMemoryUsage =
			 * ManagementFactory.getMemoryMXBean().getHeapMemoryUsage(); long mb =
			 * (heapMemoryUsage.getUsed() / 1000);
			 */
			long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			player.getPacketSender()
					.sendConsoleMessage("Heap usage: " + Misc.insertCommasToNumber("" + used + "") + " bytes!");
		}
		if (command[0].equals("save")) {
			player.save();
		}
		if (command[0].equals("saveall")) {
			World.savePlayers();
		}

		if (command[0].equalsIgnoreCase("frame")) {
			int frame = Integer.parseInt(command[1]);
			String text = command[2];
			player.getPacketSender().sendString(frame, text);
		}

		if (command[0].equals("npc") && false) {
			int id = Integer.parseInt(command[1]);
			NPC npc = NPC.of(id, new Position(player.getPosition().getX(), player.getPosition().getY(),
					player.getPosition().getZ()));
			World.register(npc);
			// npc.setConstitution(20000);
			player.getPacketSender().sendEntityHint(npc);
			/*
			 * TaskManager.submit(new Task(5) {
			 *
			 * @Override protected void execute() { npc.moveTo(new
			 * Position(npc.getPosition().getX() + 2, npc.getPosition().getY() + 2));
			 * player.getPacketSender().sendEntityHintRemoval(false); stop(); }
			 *
			 * });
			 */
			// npc.getMovementCoordinator().setCoordinator(new
			// Coordinator().setCoordinate(true).setRadius(5));
		}
		if (command[0].equals("playnpc")) {

			player.setNpcTransformationId(Integer.parseInt(command[1]));

			player.getUpdateFlag().flag(Flag.APPEARANCE);

		} else if (command[0].equals("loopanimobj")) {

			TaskManager.submit(new Task(1, player, false) {

				int id = 475;

				public void execute() {
					player.getPacketSender().sendObjectAnimation(new GameObject(1817, player.getPosition().copy(), 4),
							new Animation(id));
					player.getUpdateFlag().flag(Flag.APPEARANCE);

					id++;

					player.sendMessage("Testing anim : " + id);

					if (id == 525) {
						stop();
					}
				}
			});

		} else if (command[0].equals("playobject")) {

			player.getPacketSender().sendObjectAnimation(new GameObject(132687, player.getPosition().copy(), 10),
					new Animation(23539));
			player.getUpdateFlag().flag(Flag.APPEARANCE);

		}

		if (command[0].equals("interface")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendInterface(id);
		}

		if (command[0].equals("winterface")) {
			int id = Integer.parseInt(command[1]);
			player.sendParallellInterfaceVisibility(id, true);
		}
		if (command[0].equals("swi")) {
			int id = Integer.parseInt(command[1]);
			boolean vis = Boolean.parseBoolean(command[2]);
			player.sendParallellInterfaceVisibility(id, vis);
			player.getPacketSender().sendMessage("Done.");
		}
		if (command[0].equals("walkableinterface")) {
			int id = Integer.parseInt(command[1]);
			player.sendParallellInterfaceVisibility(id, true);
		}
		if (command[0].equals("anim")) {
			boolean osrs = false;

			if (command.length > 2) {
				osrs = Boolean.parseBoolean(command[2]);
			}

			int id = Integer.parseInt(command[1]);
			player.performAnimation(new Animation(id, osrs));
			player.getPacketSender().sendConsoleMessage("Sending animation: " + id);
		}
		if (command[0].equals("gfx")) {
			boolean osrs = false;

			if (command.length > 2) {
				osrs = Boolean.parseBoolean(command[2]);
			}

			int id = Integer.parseInt(command[1]);
			player.performGraphic(new Graphic(id, osrs));
			player.getPacketSender().sendConsoleMessage("Sending graphic: " + id);
		}
		if (command[0].startsWith("pnpc")) {
			boolean osrs = false;

			if (command.length > 2) {
				osrs = Boolean.parseBoolean(command[2]);
			}

			int id = Integer.parseInt(command[1]);
			player.setNpcTransformationId(osrs ? GameSettings.OSRS_NPC_OFFSET + id : id);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendConsoleMessage("Setting pnpc: " + id);
		}
		if (command[0].equals("object")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendObject(new GameObject(id, player.getPosition(), 10, 2));
			player.getPacketSender().sendConsoleMessage("Sending object: " + id);
		}
		if (command[0].equals("config")) {
			int id = Integer.parseInt(command[1]);
			int state = Integer.parseInt(command[2]);
			player.getPacketSender().sendConfig(id, state).sendConsoleMessage("Sent config.");
		}

	}

}