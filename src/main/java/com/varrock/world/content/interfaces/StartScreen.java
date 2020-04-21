package com.varrock.world.content.interfaces;

import com.varrock.GameServer;
import com.varrock.model.GameMode;
import com.varrock.model.Item;
import com.varrock.world.World;
import com.varrock.world.content.clan.ClanChatManager;
import com.varrock.world.entity.impl.player.Player;

/**
 * Start screen functionality.
 *
 * @author Joey wijers
 */
public class StartScreen {
    /*public enum GameModes {

        NORMAL("Normal",
        		67012, 67008, 1, 0, 
        		new Item[]{
        				new Item(2441, 10),
        				new Item(2437, 10), 
        				new Item(2443, 10), 
        				new Item(3025, 10), 
        				new Item(392, 200), 
        				new Item(995, 1000000),
        				new Item(1153, 1), 
        				new Item(1115, 1), 
        				new Item(1067, 1), 
        				(new Item(1191, 1)),
        				(new Item(1323, 1)), 
        				(new Item(4391, 1)), 
        				(new Item(841, 1)),
        				(new Item(882, 500)),
        				(new Item(1169, 1)), 
        				(new Item(1129, 1)), 
        				(new Item(1095, 1)), 
        				(new Item(1712, 1)), 
        				(new Item(579, 1)), 
        				(new Item(577, 1)), 
        				(new Item(1011, 1)), 
        				(new Item(1381, 1)), 
        				(new Item(558, 1000)), 
        				(new Item(557, 1000)), 
        				(new Item(555, 1000)), 
        				(new Item(554, 1000))},
        		"As a normal player you will be able to\\nplay the game without any restrictions.",
        		"Combat - 10x\\nSkilling - 25x\\nPost 99 - 10x"),
        IRONMAN("Ironman", 
        		67013, 67009, 1, 1, 
        		new Item[]{
        				new Item(2441, 10), 
        				new Item(2437, 10),
        				new Item(2443, 10), 
        				new Item(3025, 10), 
        				new Item(392, 200), 
        				new Item(995, 1000000), 
        				new Item(1153, 1), 
        				new Item(1115, 1), 
        				new Item(1067, 1), 
        				(new Item(1191, 1)),
        				(new Item(1323, 1)), 
        				(new Item(4391, 1)), 
        				(new Item(841, 1)), 
        				(new Item(882, 500)), 
        				(new Item(1169, 1)),
        				(new Item(1129, 1)), 
        				(new Item(1095, 1)), 
        				(new Item(1712, 1)), 
        				(new Item(579, 1)), 
        				(new Item(577, 1)), 
        				(new Item(1011, 1)), 
        				(new Item(1381, 1)), 
        				(new Item(558, 1000)),
        				(new Item(557, 1000)), 
        				(new Item(555, 1000)),
        				(new Item(554, 1000))},
        		"Play Varrock as an Iron man. You will be\\nrestricted from trading, staking, and looting\\ndrops if another player does more damage. Rely\\non gathering your own resources. This game mode\\nis for players that love a challenge!",
        		"Combat - 10x\\nSkilling - 25x\\nPost 99 - 10x"),
        ULTIMATE_IRON("Ultimate Iron", 
        		67014, 67010, 1, 2, 
        		new Item[]{
        				new Item(2441, 10), 
        				new Item(2437, 10), 
        				new Item(2443, 10), 
        				new Item(3025, 10), 
        				new Item(392, 200), 
        				new Item(995, 1000000),
        				new Item(1153, 1), 
        				new Item(1115, 1), 
        				new Item(1067, 1), 
        				(new Item(1191, 1)), 
        				(new Item(1323, 1)), 
        				(new Item(4391, 1)), 
        				(new Item(841, 1)), 
        				(new Item(882, 500)),
        				(new Item(1169, 1)), 
        				(new Item(1129, 1)),
        				(new Item(1095, 1)),
        				(new Item(1712, 1)),
        				(new Item(579, 1)), 
        				(new Item(577, 1)), 
        				(new Item(1011, 1)),
        				(new Item(1381, 1)),
        				(new Item(558, 1000)),
        				(new Item(557, 1000)), 
        				(new Item(555, 1000)), 
        				(new Item(554, 1000))},
        		"Play Varrock as a Ultimate Ironman.\\nIn addition to the regular Ironman rules, an\\nUltimate Ironman can not use banks! Keep your\\ninventory organized in order to effectively\\nprogress.",
        		"Combat - 10x\\nSkilling - 25x\\nPost 99 - 10x"),

        HARDCORE_IRON("Hardcore Iron",
                67015, 67011, 1, 4,
                new Item[]{
                        new Item(2441, 10),
                        new Item(2437, 10),
                        new Item(2443, 10),
                        new Item(3025, 10),
                        new Item(392, 200),
                        new Item(995, 1000000),
                        new Item(50792, 1),
                        new Item(50794, 1),
                        new Item(50796, 1),
                        new Item(1323, 1),
                        new Item(4391, 1),
                        new Item(841, 1),
                        (new Item(882, 500)),
                        (new Item(1169, 1)),
                        (new Item(1129, 1)),
                        (new Item(1095, 1)),
                        (new Item(1712, 1)),
                        (new Item(579, 1)),
                        (new Item(577, 1)),
                        (new Item(1011, 1)),
                        (new Item(1381, 1)),
                        (new Item(558, 1000)),
                        (new Item(557, 1000)),
                        (new Item(555, 1000)),
                        (new Item(554, 1000))},
                "Play Varrock as a Hardcore Ironman\\nIn addition to regular Ironman rules, a Hardcore\\nIronman will be reverted to a regular Ironman if\\nthey die! Take an extra precaution while you play\\nto preserve your rank. This mode is not for\\ncasuals!",
        		"Combat - 10x\\nSkilling - 25x\\nPost 99 - 10x"),
    	
    	GROUP_IRON("Group Iron",
                67031, 67030, 1, 5,
                new Item[]{
                        new Item(2441, 10),
                        new Item(2437, 10),
                        new Item(2443, 10),
                        new Item(3025, 10),
                        new Item(392, 200),
                        new Item(995, 1000000),
                        new Item(50792, 1),
                        new Item(50794, 1),
                        new Item(50796, 1),
                        new Item(1323, 1),
                        new Item(4391, 1),
                        new Item(841, 1),
                        (new Item(882, 500)),
                        (new Item(1169, 1)),
                        (new Item(1129, 1)),
                        (new Item(1095, 1)),
                        (new Item(1712, 1)),
                        (new Item(579, 1)),
                        (new Item(577, 1)),
                        (new Item(1011, 1)),
                        (new Item(1381, 1)),
                        (new Item(558, 1000)),
                        (new Item(557, 1000)),
                        (new Item(555, 1000)),
                        (new Item(554, 1000))},
                "Play Varrock as a Group Ironman.\\nTeam up with a small team of other Ironmen and\\nshare your resources between the group. Plan\\nahead in order to make the most progress!",
    			"Combat - 10x\\nSkilling - 25x\\nPost 99 - 10x");

        private String name;
        private int stringId;
        private int checkClick;
        private int textClick;
        private int configId;
        private Item[] starterPackItems;
        private String description;
        private String xpRates;

        private GameModes(String name, int stringId, int checkClick, int textClick, int configId, Item[] starterPackItems, String description, String xpRates) {
            this.name = name;
            this.stringId = stringId;
            this.checkClick = checkClick;
            this.textClick = textClick;
            this.configId = configId;
            this.starterPackItems = starterPackItems;
            this.description = description;
            this.xpRates = xpRates;
        }
    }

    public static void open(Player player) {
        sendNames(player);
        player.getPacketSender().sendInterface(67000);
        player.selectedGameMode = GameModes.NORMAL;
        check(player, GameModes.NORMAL);
        sendStartPackItems(player, GameModes.NORMAL);
        sendDescription(player, GameModes.NORMAL);
    }

    private static void sendDescription(Player player, GameModes mode) {
    	player.getPacketSender().sendString(67020, mode.description);
    	player.getPacketSender().sendString(67021, mode.xpRates);
    }

    private static void sendStartPackItems(Player player, GameModes mode) {
        final int START_ITEM_INTERFACE = 68001;
        for (int i = 0; i < 24; i++) {
            int id = -1;
            int amount = 0;
            try {
                id = mode.starterPackItems[i].getId();
                amount = mode.starterPackItems[i].getAmount();
            } catch (Exception e) {

            }
            player.getPacketSender().sendItemOnInterface(START_ITEM_INTERFACE + i, id, amount);
        }
    }

    public static boolean handleButton(Player player, int buttonId) {
        final int CONFIRM = 67023;
        if (buttonId == CONFIRM) {
            if (player.didReceiveStarter()) {

                return true;
            }

            player.getPacketSender().sendInterfaceRemoval();

            handleConfirm(player);
            if (!player.didReceiveStarter()) {
                addStarterToInv(player);
                player.setReceivedStarter(true);
            }
            player.setHidePlayer(false);
            ClanChatManager.join(player, "help"); // too many players now, rip lol
            player.getPacketSender().sendInterface(3559);
            player.getAppearance().setCanChangeAppearance(true);

            if(GameServer.starterHandler.hasAnyStarter(player.getHostAddress(), player.getSuperSerialNumber()))
                World.sendMessage("<img=732><shad=2F60B2>[New Player] " + player.getName() + " has logged into Varrock for the first time!");

            GameServer.starterHandler.addStarter(player.getHostAddress(), player.getSuperSerialNumber());

            ReferralCode.openReferral(player, true);

            //DialogueManager.start(player, 81);
            //return true;
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
        } else if (player.selectedGameMode == GameModes.ULTIMATE_IRON) {
            GameMode.set(player, GameMode.ULTIMATE_IRONMAN, false);
        } else if (player.selectedGameMode == GameModes.HARDCORE_IRON) {
            GameMode.set(player, GameMode.HARDCORE_IRONMAN, false);
        } else if (player.selectedGameMode == GameModes.GROUP_IRON) {
            //GameMode.set(player, GameMode.HARDCORE_IRONMAN, false);
        	GameMode.set(player, GameMode.IRONMAN, false); //Set to ironman until group iron is added
        } else {
            GameMode.set(player, GameMode.NORMAL, false);
        }
    }

    private static void addStarterToInv(Player player) {
        boolean reduced = false;
        for (Item item : player.selectedGameMode.starterPackItems) {
            int amount = item.getAmount();

            if(item.getId() == 995) {
                if(GameServer.starterHandler.hasAnyStarter(player.getHostAddress(), player.getSuperSerialNumber())) {
                    amount /= 10;
                    reduced = true;
                }
            }

            player.getInventory().add(item.getId(), amount, "Starter");
        }

        if(GameServer.starterHandler.hasAnyStarter(player.getHostAddress(), player.getSuperSerialNumber())) {
            player.getInventory().add(6199, 1);
        }

        if(reduced) {
            player.sendMessage("You have received too many starters and have received reduced coins.");
        }
    }

    private static void selectMode(Player player, GameModes mode) {
        player.selectedGameMode = mode;
        check(player, mode);
        sendStartPackItems(player, mode);
        sendDescription(player, mode);
    }

    public static void check(Player player, GameModes mode) {
        for (GameModes gameMode : GameModes.values()) {
            if (player.selectedGameMode == gameMode) {
                player.getPacketSender().sendConfig(gameMode.configId, 1);
                continue;
            }
            player.getPacketSender().sendConfig(gameMode.configId, 0);
        }
    }

    private static void sendNames(Player player) {
        for (GameModes mode : GameModes.values()) {
            player.getPacketSender().sendString(mode.stringId, mode.name);
        }
    }*/

}
