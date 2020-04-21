package com.varrock.world.content.freeforall;

import java.util.LinkedList;

import com.varrock.GameSettings;
import com.varrock.model.Flag;
import com.varrock.model.Item;
import com.varrock.model.MagicSpellbook;
import com.varrock.model.Position;
import com.varrock.model.Prayerbook;
import com.varrock.model.Skill;
import com.varrock.model.Locations.Location;
import com.varrock.model.container.impl.Equipment;
import com.varrock.model.definitions.WeaponAnimations;
import com.varrock.model.definitions.WeaponInterfaces;
import com.varrock.world.World;
import com.varrock.world.content.BonusManager;
import com.varrock.world.content.combat.magic.Autocasting;
import com.varrock.world.content.combat.weapon.CombatSpecial;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.transportation.TeleportHandler;
import com.varrock.world.entity.impl.player.Player;

/*
 * @author Ajw
 * www.simplicityps.org
 * -Handles a fun free for all system for events-
 */
public class FreeForAll {

    public static final Position GAME_START_POSITION = new Position(3313, 9843);

    public static LinkedList<Player> ffaPlayers = new LinkedList<>();
    public static boolean lobbyOpened = false;
    public static boolean portalOpened = false;
    public static String gameName;
    public static int gameType;
    public static Item reward;

    public static void initiateEvent(Player player, Integer id, Integer amount) {
        DialogueManager.start(player, 287);
        player.setDialogueActionId(133);
        if (id != null) {
            reward = new Item(id, amount != null ? amount : 1);
        }
    }

    public static void startDh(Player player) {
        gameType = 1;
        startEvent(player);
    }

    public static void startZerk(Player player) {
        gameType = 2;
        startEvent(player);
    }

    public static void startPure(Player player) {
        gameType = 3;
        startEvent(player);
    }

    public static void startF2p(Player player) {
        gameType = 4;
        startEvent(player);
    }

    public static void startDDS(Player player) {
        gameType = 5;
        startEvent(player);
    }

    public static void startRange(Player player) {
        gameType = 6;
        startEvent(player);
    }

    public static void startMonk(Player player) {
        gameType = 7;
        startEvent(player);
    }

    public static void startTorva(Player player) {
        gameType = 8;
        startEvent(player);
    }

    public static void startEvent(Player player) {
        player.getPacketSender().sendInterfaceRemoval();
        lobbyOpened = true;

        if (gameType == 1) {
            World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA EVENT ] <col=f0ff00>A dharok free for all event has started! Type ::ffa to join!");
            World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA RULES ] <col=f0ff00>No Overhead Prayers or you will be instant Killed!");
            gameName = "Dharok";
        }
        if (gameType == 2) {
            World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA EVENT ] <col=f0ff00>A zerker free for all event has started! Type ::ffa to join!");
            World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA RULES ] <col=f0ff00>No Overhead Prayers or you will be instant Killed!");
            gameName = "Zerker";
        }
        if (gameType == 3) {
            World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA EVENT ] <col=f0ff00>A pure free for all event has started! Type ::ffa to join!");
            World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA RULES ] <col=f0ff00>No Overhead Prayers or you will be instant Killed!");
            gameName = "Pure";
        }
        if (gameType == 4) {
            World.sendMessage("<img=483><col=dbffba><shad=1> [FFA EVENT] <col=f0ff00>A f2p free for all event has started! Type ::ffa to join!");
            World.sendMessage("<img=483><col=dbffba><shad=1> [FFA RULES] <col=f0ff00>No Overhead Prayers or you will be instant Killed!");
            gameName = "F2p";
        }
        if (gameType == 5) {
            World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA EVENT ] <col=f0ff00>A DDS only free for all event has started! Type ::ffa to join!");
            World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA RULES ] <col=f0ff00>No Overhead Prayers or you will be instant Killed!");
            gameName = "DDS";
        }
        if (gameType == 6) {
            World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA EVENT ] <col=f0ff00>A range only free for all event has started! Type ::ffa to join!");
            World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA RULES ] <col=f0ff00>No Overhead Prayers or you will be instant Killed!");
            gameName = "Range";
        }
        if (gameType == 7) {
            World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA EVENT ] <col=f0ff00>A Monk Robe free for all event has started! Type ::ffa to join!");
            World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA RULES ] <col=f0ff00>No Overhead Prayers or you will be instant Killed!");
            gameName = "Monkrobe";
        }
        if (gameType == 8) {
            World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA EVENT ] <col=f0ff00>A Max Melee free for all event has started! Type ::ffa to join!");
            World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA RULES ] <col=f0ff00>No Overhead Prayers or you will be instant Killed!");
            gameName = "Torva";
        }
    }

    /*
     * Initiate the lobby teleport Check for inventory or items
     */
    public static void initiateLobby(Player player) {
        if (player.getSummoning().getFamiliar() != null) {
            player.getPacketSender().sendMessage("You must disable your summoning familiar to enter!");
            return;
        }
        for (Item t : player.getEquipment().getItems()) {
            if (t != null && t.getId() > 0) {
                player.getPacketSender().sendMessage("You must bank your equipment");
                return;
            }
        }
        for (Item t : player.getInventory().getItems()) {
            if (t != null && t.getId() > 0) {
                player.getPacketSender().sendMessage("You must bank your invetory");
                return;
            }
        }

        TeleportHandler.cancelCurrentActions(player);
        startLobbyTeleport(player);
    }

    /*
     * Checks are complete Move the player to the lobby
     */
    public static void startLobbyTeleport(Player player) {
        Position position = new Position(2525, 4776);
        player.moveTo(position);
        player.getPA().sendInterfaceRemoval();
        Autocasting.resetAutocast(player, false);
        player.getPacketSender().sendMessage("@blu@Welcome to the @red@" + gameName + " FFA Lobby. @blu@The portal will be opened soon.");

        player.setSpecialPercentage(100);
        CombatSpecial.updateBar(player);
        player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
        player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, player.getSkillManager().getMaxLevel(Skill.CONSTITUTION), true);
        player.getPacketSender().sendMessage("Your special attack, prayer, and health has been restored.");
        giveGear(player);

        for (int i = 0; i < 7; i++) {
            if (i == 3 || i == 5)
                continue;
            if (player.getSkillManager().getCurrentLevel(Skill.forId(i)) > 99) {
                player.getSkillManager().setCurrentLevel(Skill.forId(i), 99);
                player.setOverloadPotionTimer(0);
            }
        }
    }

    /*
     * Handle lobby portal
     */
    public static void handlePortal(Player player) {
        if (portalOpened) {
            ffaPlayers.add(player);
            player.moveTo(GAME_START_POSITION, true);
        } else {
            player.getPacketSender().sendMessage("The portal is not opened yet! The game will start shortly...");
        }
    }

    /*
     * Opens portal to allow players to access game
     */
    public static void openPortal(Player player) {
        portalOpened = true;
        World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA EVENT ] <col=f0ff00>The ::ffa portal has opened! You have 10 seconds to enter!");
        World.sendStaffMessage("<img=483><col=dbffba><shad=1> [ FFA EVENT-STAFF ] <col=f0ff00>This is a reminder to close the ffa portal when ready! ::ffaclose");
    }

    public static void closePortal(Player player) {
        portalOpened = false;
        lobbyOpened = false;
        World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA EVENT ] <col=f0ff00>The ::ffa portal has closed! Good luck to the contestors!");
        World.sendStaffMessage("<img=483><col=dbffba><shad=1> [ FFA EVENT-STAFF ] <col=f0ff00>Portal has been closed! Game is live");
    }

    /*
     * Give the gear based on the game type
     *
     */
    public static void giveGear(Player player) {
        player.previousSpellbook = player.getSpellbook();
        if (gameType == 1) {
            int[][] data = new int[][]{{Equipment.HEAD_SLOT, 4716}, {Equipment.CAPE_SLOT, 19111}, {Equipment.AMULET_SLOT, 6585}, {Equipment.WEAPON_SLOT, 4151}, {Equipment.BODY_SLOT, 4720},
                    {Equipment.SHIELD_SLOT, 20072}, {Equipment.LEG_SLOT, 4722}, {Equipment.HANDS_SLOT, 7462}, {Equipment.FEET_SLOT, 11732}, {Equipment.RING_SLOT, 6737},
                    {Equipment.AMMUNITION_SLOT, 9244}};
            for (int i = 0; i < data.length; i++) {
                int slot = data[i][0], id = data[i][1];
                player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 100 : 1));
            }
            BonusManager.update(player);
            player.setSpellbook(MagicSpellbook.LUNAR);
            player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            player.getEquipment().refreshItems();
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.getInventory().resetItems();
            player.getInventory().add(14484, 1, "FFA gear").add(4718, 1, "FFA gear").add(2442, 1, "FFA gear").add(2436, 1, "FFA gear").add(2440, 1, "FFA gear").add(6685, 1, "FFA gear")
                    .add(3024, 2, "FFA gear").add(9075, 200, "FFA gear").add(560, 100, "FFA gear").add(557, 500, "FFA gear").add(391, 20, "FFA gear");

        }
        if (gameType == 2) {
            int[][] data = new int[][]{{Equipment.HEAD_SLOT, 3751}, {Equipment.CAPE_SLOT, 19111}, {Equipment.AMULET_SLOT, 6585}, {Equipment.WEAPON_SLOT, 4151},
                    {Equipment.BODY_SLOT, 10551}, {Equipment.SHIELD_SLOT, 8850}, {Equipment.LEG_SLOT, 1079}, {Equipment.HANDS_SLOT, 7462}, {Equipment.FEET_SLOT, 4131},
                    {Equipment.RING_SLOT, 6737}, {Equipment.AMMUNITION_SLOT, 9244}};
            for (int i = 0; i < data.length; i++) {
                int slot = data[i][0], id = data[i][1];
                player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 100 : 1));
            }
            BonusManager.update(player);
            player.setSpellbook(MagicSpellbook.LUNAR);
            player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            player.getEquipment().refreshItems();
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.getInventory().resetItems();
            player.getInventory().add(11694, 1, "FFA gear").add(18353, 1, "FFA gear").add(2442, 1, "FFA gear").add(2436, 1, "FFA gear").add(2440, 1, "FFA gear").add(6685, 1, "FFA gear")
                    .add(3024, 2, "FFA gear").add(9075, 200, "FFA gear").add(560, 100, "FFA gear").add(557, 500, "FFA gear").add(391, 20, "FFA gear");

        }
        if (gameType == 3) {
            int[][] data = new int[][]{{Equipment.HEAD_SLOT, 1153}, {Equipment.CAPE_SLOT, 6570}, {Equipment.AMULET_SLOT, 6585}, {Equipment.WEAPON_SLOT, 4151}, {Equipment.BODY_SLOT, 1115},
                    {Equipment.SHIELD_SLOT, 3842}, {Equipment.LEG_SLOT, 2497}, {Equipment.HANDS_SLOT, 7459}, {Equipment.FEET_SLOT, 2577}, {Equipment.RING_SLOT, 2550},
                    {Equipment.AMMUNITION_SLOT, 9244}};
            for (int i = 0; i < data.length; i++) {
                int slot = data[i][0], id = data[i][1];
                player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 100 : 1));
            }
            BonusManager.update(player);
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            player.getEquipment().refreshItems();
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.getInventory().resetItems();
            player.getInventory().add(5680, 1, "FFA gear").add(9185, 1, "FFA gear").add(2442, 1, "FFA gear").add(2436, 1, "FFA gear").add(2440, 1, "FFA gear").add(6685, 1, "FFA gear")
                    .add(3024, 2, "FFA gear").add(391, 20, "FFA gear");

        }
        if (gameType == 4) {
            int[][] data = new int[][]{{Equipment.HEAD_SLOT, 1169}, {Equipment.CAPE_SLOT, 6568}, {Equipment.AMULET_SLOT, 1725}, {Equipment.WEAPON_SLOT, 853}, {Equipment.BODY_SLOT, 544},
                    {Equipment.LEG_SLOT, 1099}, {Equipment.HANDS_SLOT, 1065}, {Equipment.FEET_SLOT, 1061}, {Equipment.RING_SLOT, 2550}, {Equipment.AMMUNITION_SLOT, 890}};
            for (int i = 0; i < data.length; i++) {
                int slot = data[i][0], id = data[i][1];
                player.getEquipment().setItem(slot, new Item(id, id == 890 ? 200 : 1));
            }
            BonusManager.update(player);
            player.setPrayerbook(Prayerbook.NORMAL);
            player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB, player.getPrayerbook().getInterfaceId());
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            player.getEquipment().refreshItems();
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.getInventory().resetItems();
            player.getInventory().add(1333, 1, "FFA gear").add(1319, 1, "FFA gear").add(113, 1, "FFA gear").add(2434, 1, "FFA gear").add(373, 24, "FFA gear");

        }
        if (gameType == 5) {
            int[][] data = new int[][]{{Equipment.HEAD_SLOT, -1}, {Equipment.CAPE_SLOT, -1}, {Equipment.AMULET_SLOT, -1}, {Equipment.WEAPON_SLOT, 1215}, {Equipment.BODY_SLOT, -1},
                    {Equipment.SHIELD_SLOT, -1}, {Equipment.LEG_SLOT, -1}, {Equipment.HANDS_SLOT, 7462}, {Equipment.FEET_SLOT, -1}, {Equipment.RING_SLOT, -1},
                    {Equipment.AMMUNITION_SLOT, -1}};
            for (int i = 0; i < data.length; i++) {
                int slot = data[i][0], id = data[i][1];
                player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 100 : 1));
            }
            BonusManager.update(player);
            player.setSpellbook(MagicSpellbook.LUNAR);
            player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            player.getEquipment().refreshItems();
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.getInventory().resetItems();
            player.getInventory().add(2440, 1, "FFA gear").add(2436, 1, "FFA gear");

        }
        if (gameType == 6) {
            int[][] data = new int[][]{{Equipment.HEAD_SLOT, 3749}, {Equipment.CAPE_SLOT, 10499}, {Equipment.AMULET_SLOT, 15126}, {Equipment.WEAPON_SLOT, 13051},
                    {Equipment.BODY_SLOT, 4736}, {Equipment.SHIELD_SLOT, 3842}, {Equipment.LEG_SLOT, 4738}, {Equipment.HANDS_SLOT, 7462}, {Equipment.FEET_SLOT, 10696},
                    {Equipment.RING_SLOT, 15019}, {Equipment.AMMUNITION_SLOT, 9244}};
            for (int i = 0; i < data.length; i++) {
                int slot = data[i][0], id = data[i][1];
                player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 100 : 1));
            }
            BonusManager.update(player);
            player.setSpellbook(MagicSpellbook.LUNAR);
            player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            player.getEquipment().refreshItems();
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.getInventory().resetItems();
            player.getInventory().add(11235, 1, "FFA gear").add(11212, 50, "FFA gear").add(15324, 1, "FFA gear").add(6685, 1, "FFA gear").add(3024, 2, "FFA gear").add(9075, 200, "FFA gear")
                    .add(560, 100, "FFA gear").add(557, 500, "FFA gear").add(391, 20, "FFA gear");

        }
        if (gameType == 7) {
            int[][] data = new int[][]{{Equipment.HEAD_SLOT, -1}, {Equipment.CAPE_SLOT, 6570}, {Equipment.AMULET_SLOT, 6585}, {Equipment.WEAPON_SLOT, 4151}, {Equipment.BODY_SLOT, 544},
                    {Equipment.SHIELD_SLOT, 20072}, {Equipment.LEG_SLOT, 542}, {Equipment.HANDS_SLOT, 7462}, {Equipment.FEET_SLOT, 11732}, {Equipment.RING_SLOT, 6737},
                    {Equipment.AMMUNITION_SLOT, 9244}};
            for (int i = 0; i < data.length; i++) {
                int slot = data[i][0], id = data[i][1];
                player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 100 : 1));
            }
            BonusManager.update(player);
            player.setSpellbook(MagicSpellbook.LUNAR);
            player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            player.getEquipment().refreshItems();
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.getInventory().resetItems();
            player.getInventory().add(1215, 1, "FFA gear").add(3024, 2, "FFA gear").add(2436, 1, "FFA gear").add(2440, 1, "FFA gear").add(6685, 1, "FFA gear").add(557, 200, "FFA gear")
                    .add(560, 100, "FFA gear").add(9075, 500, "FFA gear").add(391, 20, "FFA gear");

        }
        if (gameType == 8) {
            int[][] data = new int[][]{{Equipment.HEAD_SLOT, 14008}, {Equipment.CAPE_SLOT, 14022}, {Equipment.AMULET_SLOT, 17291}, {Equipment.WEAPON_SLOT, 16955},
                    {Equipment.BODY_SLOT, 14009}, {Equipment.SHIELD_SLOT, 11613}, {Equipment.LEG_SLOT, 14010}, {Equipment.HANDS_SLOT, 7462}, {Equipment.FEET_SLOT, 13239},
                    {Equipment.RING_SLOT, 12601}, {Equipment.AMMUNITION_SLOT, 9244}};
            for (int i = 0; i < data.length; i++) {
                int slot = data[i][0], id = data[i][1];
                player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 100 : 1));
            }
            BonusManager.update(player);
            player.setSpellbook(MagicSpellbook.LUNAR);
            player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            player.getEquipment().refreshItems();
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.getInventory().resetItems();
            player.getInventory().add(11694, 1, "FFA gear").add(18353, 1, "FFA gear").add(2442, 1, "FFA gear").add(2436, 1, "FFA gear").add(2440, 1, "FFA gear").add(6685, 1, "FFA gear")
                    .add(3024, 2, "FFA gear").add(9075, 200, "FFA gear").add(560, 100, "FFA gear").add(557, 500, "FFA gear").add(391, 20, "FFA gear");

        }
    }

    /*
     * Leading the arena on death Reset the items and location
     */
    public static void leaveArena(Player player) {
        player.getEquipment().resetItems().refreshItems();
        player.getInventory().resetItems().refreshItems();
        WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
        WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
        if(player.previousSpellbook != null) {
            player.setSpellbook(player.previousSpellbook);
            player.previousSpellbook = null;
        }
        BonusManager.update(player);
        player.getUpdateFlag().flag(Flag.APPEARANCE);
        ffaPlayers.remove(player);

        if(!player.isStaff())
            player.moveTo(GameSettings.DEFAULT_POSITION, true);

        checkForWinner(player);
    }

    public static void staffLeaveArena(Player player) {
        player.getEquipment().resetItems().refreshItems();
        player.getInventory().resetItems().refreshItems();
        WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
        WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
        if(player.previousSpellbook != null) {
            player.setSpellbook(player.previousSpellbook);
            player.previousSpellbook = null;
        }
        BonusManager.update(player);
        player.getUpdateFlag().flag(Flag.APPEARANCE);
        ffaPlayers.remove(player);

        player.moveTo(GameSettings.DEFAULT_POSITION, true);

        checkForWinner(player);
    }

    private static void checkForWinner(Player player) {
        if (ffaPlayers.size() == 1) {
            Player winner = ffaPlayers.removeFirst();
            World.sendMessage("<img=483><col=dbffba><shad=1> [ FFA EVENT ]</col> " + winner.getName() + " has won the FFA event!");
            String extra = reward != null ? " You have received "
                    + reward.getDefinition().getName() +
                    " (x" + reward.getAmount() + ")." : "";
            winner.sendMessage("@red@You have won the FFA event!" + extra);
            if (reward != null)
                player.getInventory().add(reward);
            winner.getEquipment().resetItems().refreshItems();
            winner.getInventory().resetItems().refreshItems();
            winner.getUpdateFlag().flag(Flag.APPEARANCE);
            winner.moveTo(GameSettings.DEFAULT_POSITION, true);

            // Move staff back to home.
            for(Player other : World.getPlayers()) {
                if(other != null && other.getLocation() == Location.FFAARENA) {
                    other.moveTo(GameSettings.DEFAULT_POSITION, true);
                }
            }
        }
    }
}
