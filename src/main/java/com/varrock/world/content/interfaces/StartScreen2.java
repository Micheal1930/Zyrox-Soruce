package com.varrock.world.content.interfaces;

import com.varrock.GameServer;
import com.varrock.model.GameMode;
import com.varrock.model.Item;
import com.varrock.model.XPMode;
import com.varrock.world.World;
import com.varrock.world.content.clan.ClanChatManager;
import com.varrock.world.entity.impl.player.Player;

public class StartScreen2 {

	public enum GameModes {
		NORMAL("Normal", 52761, 28003, 1, 0, new Item[]{
				new Item(995, 500000), new Item(556, 500), new Item(555, 500), new Item(557, 500), //
				new Item(554, 250), new Item(558, 500), new Item(562, 500), new Item(563, 500), //
				new Item(1323, 1), new Item(841, 1), new Item(882, 500), new Item(10499, 1), //
				new Item(380, 250), new Item(544, 1), new Item(542, 1)//
		}, "As a normal player you will be", "able to play the game without", "any restrictions.", "", "", "", "", "",
				"x25", "x150"),
		IRONMAN("<img=33>Ironman", 52762, 28004, 1, 1, new Item[]{
				new Item(995, 100000), new Item(556, 500), new Item(555, 500), new Item(557, 500), //
				new Item(554, 500), new Item(558, 500), new Item(562, 500), new Item(563, 500), //
				new Item(1323, 1), new Item(841, 1), new Item(882, 500), new Item(10499, 1), //
				new Item(380, 50), new Item(544, 1), new Item(542, 1)//
		}, "Play Varrock as an Iron man.", "With this gamemode you must", "be self sufficient",
				"You will be restricted from:", "-Staking and Gambling.",
				"-Looting items from killed players", "-Receiving loot from monsters",
				" that another player killed.", "x25",
				"x150"),
		ULTIMATE_IRONMAN("<img=31> Ultimate Ironman", 52762, 28005, 1, 1, new Item[]{
				new Item(995, 100000), new Item(556, 500), new Item(555, 500), new Item(557, 500), //
				new Item(554, 500), new Item(558, 500), new Item(562, 500), new Item(563, 500), //
				new Item(1323, 1), new Item(841, 1), new Item(882, 500), new Item(10499, 1), //
				new Item(380, 50), new Item(544, 1), new Item(542, 1)//
		}, "Play Varrock as an Iron man.", "With this gamemode you must", "be self sufficient",
				"You will be restricted from:", "-Staking and Gambling.",
				"-Looting items from killed players", "-Receiving loot from monsters",
				" that another player killed.", "x25",
				"x150"),
		HC_IRONMAN("<img=32> HC Iron", 52763, 28006, 1, 2, new Item[]{
				new Item(995, 100000), new Item(556, 500), new Item(555, 500),
				new Item(557, 500),
				new Item(554, 500), new Item(558, 500), new Item(562, 500), new Item(563, 500),
				new Item(1323, 1), new Item(841, 1), new Item(882, 500), new Item(10499, 1),
				new Item(380, 50), new Item(50792, 1), new Item(50794, 1), new Item(50796, 1),
		}, "Play Varrock as a Hardcore Ironman", "In addition to the iron man mode,",
				"@red@If you die you will lose your status.", "", "", "", "", "", "x25",
				"x150");
		private int checkClick;
		private int textClick;
		private int configId;
		private Item[] starterPackItems;
		private String line1;
		private String line2;
		private String line3;
		private String line4;
		private String line5;
		private String line6;
		private String line7;
		private String line8;

		private String skillExp;
		private String cmbExp;

		GameModes(String name, int stringId, int checkClick, int textClick, int configId,
				  Item[] starterPackItems, String line1, String line2, String line3, String line4, String line5,
				  String line6, String line7, String line8, String skillExp, String cmbExp) {
			this.checkClick = checkClick;
			this.textClick = textClick;
			this.configId = configId;
			this.starterPackItems = starterPackItems;
			this.line1 = line1;
			this.line2 = line2;
			this.line3 = line3;
			this.line4 = line4;
			this.line5 = line5;
			this.line6 = line6;
			this.line7 = line7;
			this.line8 = line8;

			this.skillExp = skillExp;
			this.cmbExp = cmbExp;

		}
	}

	public static void open(Player player) {
		player.getPacketSender().sendInterface(28000);
		player.selectedGameMode = GameModes.NORMAL;
		check(player, GameModes.NORMAL);
		player.getPacketSender().sendConfig(1590, 0);
		sendStartPackItems(player, GameModes.NORMAL);
		sendDescription(player, GameModes.NORMAL);
	}

	private static void sendDescription(Player player, GameModes mode) {
		int s = 47951;
		player.getPacketSender().sendString(s, mode.line1);
		player.getPacketSender().sendString(s + 1, mode.line2);
		player.getPacketSender().sendString(s + 2, mode.line3);
		player.getPacketSender().sendString(s + 3, mode.line4);
		player.getPacketSender().sendString(s + 4, mode.line5);
		player.getPacketSender().sendString(s + 5, mode.line6);
		player.getPacketSender().sendString(s + 6, mode.line7);
		player.getPacketSender().sendString(s + 7, mode.line8);
		player.getPacketSender().sendString(s + 7, mode.line8);

		player.getPacketSender().sendString(28022, mode.skillExp);
		player.getPacketSender().sendString(28023, mode.cmbExp);
	}

	private static void sendStartPackItems(Player player, GameModes mode) {
		final int START_ITEM_INTERFACE = 28201;
		for (int i = 0; i < 28; i++) {
			if (i < mode.starterPackItems.length) {
				int id = mode.starterPackItems[i].getId();
				int amount = mode.starterPackItems[i].getAmount();
				player.getPacketSender().sendItemOnInterface(START_ITEM_INTERFACE + i, id, amount);
			} else {
				player.getPacketSender().sendItemOnInterface(START_ITEM_INTERFACE + i, -1, -1);
			}

		}
	}

	public static void handleDropdown(Player player, int identifier) {
		switch (identifier) {
			case 0:
				player.selectedXPMode = XPMode.REGULAR;
				break;
			case 1:
				player.selectedXPMode = XPMode.CLASSIC;
				break;
		}
	}

	public static boolean handleButton(Player player, int buttonId) {
		final int CONFIRM = 28007;
		if (buttonId == CONFIRM) {
			if (player.didReceiveStarter()) {
				return true;
			}
			player.getPacketSender().sendInterfaceRemoval();

			handleConfirm(player);

			player.setPlayerLocked(false);
			player.setHidePlayer(false);
			player.getPacketSender().sendInterface(3559);
			player.getAppearance().setCanChangeAppearance(true);
			player.setNewPlayer(false);
			ClanChatManager.join(player, "Help");
			World.sendMessage("<img=732><shad=2F60B2>[New Player] " + player.getName() + " has logged into Varrock for the first time!");
			if (GameServer.starterHandler.hasAnyStarter(player.getHostAddress(), player.getSuperSerialNumber())) {
				if (!player.didReceiveStarter()) {
					addStarterToInv(player);
					player.setReceivedStarter(true);
				}
				/*if (player.isAcceptedTutorial()) {
					if (player.getGameMode() == GameMode.CLASSIC || player.getGameMode() == GameMode.NORMAL)
						player.getInventory().add(995, 250000);
					else
						player.getInventory().add(995, 50000);
				}*/
			} else {
				player.getPacketSender().sendMessage("You've recieved to many starters.");
			}

			GameServer.starterHandler.addStarter(player.getHostAddress(), player.getSuperSerialNumber());

		}
		for (GameModes mode : GameModes.values()) {
			if (mode.checkClick == buttonId || mode.textClick == buttonId) {
				selectMode(player, mode);
				return true;
			}
		}
		return false;

	}

	private static void handleConfirm(Player player) {
        if (player.selectedGameMode == GameModes.IRONMAN) {
            GameMode.set(player, GameMode.IRONMAN, false);
        } else if (player.selectedGameMode == GameModes.ULTIMATE_IRONMAN) {
            GameMode.set(player, GameMode.ULTIMATE_IRONMAN, false);
        } else if (player.selectedGameMode == GameModes.HC_IRONMAN) {
            GameMode.set(player, GameMode.HARDCORE_IRONMAN, false);
        } else {
            GameMode.set(player, GameMode.NORMAL, false);
        }
        player.setXPMode(player.selectedXPMode);
    }

	private static void addStarterToInv(Player player) {
		for (Item item : player.selectedGameMode.starterPackItems) {
			player.getInventory().add(item);
		}
	}

	private static void selectMode(Player player, GameModes mode) {
		player.selectedGameMode = mode;
		check(player, mode);
		sendStartPackItems(player, mode);
		sendDescription(player, mode);
	}

	private static void check(Player player, GameModes mode) {
		for (GameModes gameMode : GameModes.values()) {
			if (player.selectedGameMode == gameMode) {
				player.getPacketSender().sendConfig(gameMode.configId, 1);
				continue;
			}
			player.getPacketSender().sendConfig(gameMode.configId, 0);
		}
	}

}
