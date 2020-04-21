package com.varrock.world.content.skill.impl.smithing;

import com.varrock.model.Item;
import com.varrock.model.Skill;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.model.input.impl.EnterAmountOfBarsToSmelt;
import com.varrock.util.Misc;
import com.varrock.world.content.ItemDegrading;
import com.varrock.world.entity.impl.player.Player;

import java.util.ArrayList;

public class SmithingData {


    public static final int[] SMELT_BARS = {2349, 2351, 2355, 2353, 2357, 2359, 2361, 2363};
    public static final int[] SMELT_FRAME = {2405, 2406, 2407, 2409, 2410, 2411, 2412, 2413};

    public static int[] SmithingItems = new int[]{9140, 877, 1205, 1351, 1422, 1139, 9375, 1277, 4819, 1794, 819, 39, 1321, 1265, 1291, 9420, 1155, 864, 1173,
            1337, 1375, 1103, 1189, 3095, 1307, 1087, 1075, 1117, 1203, 15298, 1420, 7225, 1137, 9140, 1279, 4820, 820, 40, 1323, 1267,
            1293, 1153, 863, 1175, 1349, 9423, 1335, 1363, 1101, 4540, 1191, 3096, 1309, 1081, 1067, 1115, 1207, 1353, 1424, 1141, 9141, 1539,
            1281, 821, 41, 1325, 1269, 1295, 2370, 9425, 1157, 865, 1177, 1339, 1365, 1105, 1193, 3097, 1311, 1084, 1069, 1119, 1209,
            1355, 1428, 1143, 9142, 1285, 4822, 822, 42, 1329, 1273, 1299, 9427, 1159, 866, 1181, 1343, 9416, 1369, 1109, 1197, 3099,
            1315, 1085, 1071, 1121, 1211, 1357, 1430, 1145, 9143, 1287, 4823, 823, 43, 1331, 1271, 1301, 9429, 1161, 867, 1183, 1345, 1371,
            1111, 1199, 3100, 1317, 1091, 1073, 1123, 1213, 1359, 1432, 1147, 9144, 1289, 4824, 824, 44, 1333, 1275, 1303, 9431, 1163, 868,
            1185, 1347, 1373, 1113, 1201, 3101, 1319, 1093, 1079, 1127, 2, 1083};

    //BarId, Ore1, Ore2, Levelreq, XP
    public static final double[][] SmeltData = {
            {2349, 438, 436, 1, 1}, // Bronze bar
            {2351, 440, -1, 15, 12.5}, // Iron bar
            {2355, 442, -1, 20, 13.67}, // Silver bar
            {2353, 440, 453, 30, 17.5}, // Steel bar
            {2357, 444, -1, 40, 22.5}, // Gold bar
            {2359, 447, 453, 50, 30}, // Mithril bar
            {2361, 449, 453, 70, 37.5}, // Adamantite bar
            {2363, 451, 453, 85, 50} // Runite bar
    };

    /*
     * Gets the ores needed and stores them in to the array so we can use them.
     */
    public static double[] getOres(int bar) {
        double[] ores = new double[2];
        for (int i = 0; i < SmeltData.length; i++) {
            if (SmeltData[i][0] == bar) {

                double ore1 = SmeltData[i][1];
                double ore2 = SmeltData[i][2];

                ores[0] = ore1;
                ores[1] = ore2;
            }
        }
        return ores;
    }

    /*
     * Checks if a player has ores required for a certain barId
     */
    public static boolean hasOres(Player player, int barId) {
        player.setOres(getOres(barId)); //Insert ores ids to the array
        if (player.getOres()[0] > 0 && player.getOres()[1] < 0) {
            if (player.getInventory().getAmount((int) player.getOres()[0]) > 0)
                return true;
        } else if (player.getOres()[1] > 0 && player.getOres()[1] != 453 && player.getOres()[0] > 0) {
            if (player.getInventory().getAmount((int) player.getOres()[1]) > 0 && player.getInventory().getAmount((int) player.getOres()[0]) > 0)
                return true;
        } else if (player.getOres()[1] > 0 && player.getOres()[1] == 453 && player.getOres()[0] > 0) {
            if (player.getInventory().getAmount((int) player.getOres()[1]) >= getCoalAmount(barId) && player.getInventory().getAmount((int) player.getOres()[0]) > 0)
                return true;
        }
        return false;

    }


    /*
     * Checks if a player has required stats to smelt certain barId
     */
    public static boolean canSmelt(Player player, int barId) {
        if (getLevelReq(barId) > player.getSkillManager().getMaxLevel(Skill.SMITHING)) {
            player.getPacketSender().sendMessage("You need a Smithing level of at least " + String.format("%.0f", getLevelReq(barId)) + " to make this bar.");
            return false;
        }
        if (!hasOres(player, barId)) {
            player.getPacketSender().sendMessage("You do not have the required ores to make this bar.");
            String requirement = null;

            if (player.getOres()[0] > 0 && player.getOres()[1] > 0 && player.getOres()[1] != 453) {
                requirement = "To make " + anOrA(barId) + " " + new Item(barId).getDefinition().getName() + ", you need some " + new Item((int) player.getOres()[0]).getDefinition().getName().replace(" ore", "") + " and " + new Item((int) player.getOres()[1]).getDefinition().getName() + ".";
            } else if (player.getOres()[0] > 0 && player.getOres()[1] == -1) {
                requirement = "To make " + anOrA(barId) + " " + new Item(barId).getDefinition().getName() + ", you need some " + new Item((int) player.getOres()[0]).getDefinition().getName() + ".";
            } else if (player.getOres()[0] > 0 && player.getOres()[1] == 453) { //The bar uses custom coal amount
                requirement = "To make " + anOrA(barId) + " " + new Item(barId).getDefinition().getName() + ", you need some " + new Item((int) player.getOres()[0]).getDefinition().getName().replace(" ore", "") + " and " + getCoalAmount(barId) + " " + new Item((int) player.getOres()[1]).getDefinition().getName() + " ores.";
            }

            if (requirement != null)
                player.getPacketSender().sendMessage(requirement);

            return false;
        }
        return true;
    }

    /*
     * Gets the correct 'message'
     */
    public static String anOrA(int barId) {
        if (barId == 2351 || barId == 2361) // Iron and Adamantite bars
            return "an";
        return "a";
    }

    /*
     * Gets Smithing level required for a certain barId
     */
    public static double getLevelReq(int barId) {
        for (int i = 0; i < SmeltData.length; i++) {
            if (SmeltData[i][0] == barId) {
                return SmeltData[i][3];
            }
        }
        return 1;
    }

    /*
     * Handles the Smithing interfaces and their buttons
     */
    public static boolean handleButtons(Player player, int id) {
        switch (id) {
            /*
             * Bronze Smelting
             */
            case 3987:
                Smelting.smeltBar(player, 2349, 1);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 3986:
                Smelting.smeltBar(player, 2349, 5);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 2807:
                Smelting.smeltBar(player, 2349, 10);
                player.getPacketSender().sendInterfaceRemoval();
                return true;

            /*
             * Iron Smelting
             */
            case 3991:
                Smelting.smeltBar(player, 2351, 1);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 3990:
                Smelting.smeltBar(player, 2351, 5);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 3989:
                Smelting.smeltBar(player, 2351, 10);
                player.getPacketSender().sendInterfaceRemoval();
                return true;

            /*
             * Silver Smelting
             */
            case 3995:
                Smelting.smeltBar(player, 2355, 1);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 3994:
                Smelting.smeltBar(player, 2355, 5);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 3993:
                Smelting.smeltBar(player, 2355, 10);
                player.getPacketSender().sendInterfaceRemoval();
                return true;

            /*
             * Steel Smelting
             */
            case 3999:
                Smelting.smeltBar(player, 2353, 1);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 3998:
                Smelting.smeltBar(player, 2353, 5);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 3997:
                Smelting.smeltBar(player, 2353, 10);
                player.getPacketSender().sendInterfaceRemoval();
                return true;

            /*
             * Gold Smelting
             */
            case 4003:
                Smelting.smeltBar(player, 2357, 1);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 4002:
                Smelting.smeltBar(player, 2357, 5);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 4001:
                Smelting.smeltBar(player, 2357, 10);
                player.getPacketSender().sendInterfaceRemoval();
                return true;

            /*
             * Mithril Smelting
             */
            case 7441:
                Smelting.smeltBar(player, 2359, 1);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 7440:
                Smelting.smeltBar(player, 2359, 5);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 6397:
                Smelting.smeltBar(player, 2359, 10);
                player.getPacketSender().sendInterfaceRemoval();
                return true;

            /*
             * Adamantite Smelting
             */
            case 7446:
                Smelting.smeltBar(player, 2361, 1);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 7444:
                Smelting.smeltBar(player, 2361, 5);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 7443:
                Smelting.smeltBar(player, 2361, 10);
                player.getPacketSender().sendInterfaceRemoval();
                return true;

            /*
             * Runite Smelting
             */
            case 7450:
                Smelting.smeltBar(player, 2363, 1);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 7449:
                Smelting.smeltBar(player, 2363, 5);
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            case 7448:
                Smelting.smeltBar(player, 2363, 10);
                player.getPacketSender().sendInterfaceRemoval();
                return true;

            /*
             * Handle X
             */
            case 2414:
            case 3988:
            case 3992:
            case 3996:
            case 4000:
            case 4158:
            case 7442:
            case 7447:
                int bar = id == 2414 ? 2349 : id == 3988 ? 2351 : id == 3992 ? 2355 : id == 3996 ? 2353 : id == 4000 ? 2357 : id == 4158 ? 2359 : id == 7442 ? 2361 : id == 7447 ? 2363 : -1;
                if (bar > 0) {
                    player.setInputHandling(new EnterAmountOfBarsToSmelt(bar));
                    player.getPacketSender().sendEnterAmountPrompt("How many " + ItemDefinition.forId(bar).getName() + "s would you like to smelt?");
                }
                return true;
        }
        return false;
    }


    public static void handleSpecialBar(int barId, Player player, String barAction) {
        if (barAction.equalsIgnoreCase("message")) {

            return;
        }
        if (barAction.equalsIgnoreCase("delete")) {

            return;
        }
    }

    public static int getCoalAmount(int barId) {
        if (barId == 2359)
            return 4;
        if (barId == 2361)
            return 6;
        if (barId == 2363)
            return 8;
        return 2;

    }

    public static void showBronzeInterface(Player player) {
        String fiveb = GetForBars(2349, 5, player);
        String threeb = GetForBars(2349, 3, player);
        String twob = GetForBars(2349, 2, player);
        String oneb = GetForBars(2349, 1, player);
        player.getPacketSender().sendString(1112, fiveb + "5 Bars" + fiveb);
        player.getPacketSender().sendString(1109, threeb + "3 Bars" + threeb);
        player.getPacketSender().sendString(1110, threeb + "3 Bars" + threeb);
        player.getPacketSender().sendString(1118, threeb + "3 Bars" + threeb);
        player.getPacketSender().sendString(1111, threeb + "3 Bars" + threeb);
        player.getPacketSender().sendString(1095, threeb + "3 Bars" + threeb);
        player.getPacketSender().sendString(1115, threeb + "3 Bars" + threeb);
        player.getPacketSender().sendString(1090, threeb + "3 Bars" + threeb);
        player.getPacketSender().sendString(1113, twob + "2 Bars" + twob);
        player.getPacketSender().sendString(1116, twob + "2 Bars" + twob);
        player.getPacketSender().sendString(1114, twob + "2 Bars" + twob);
        player.getPacketSender().sendString(1089, twob + "2 Bars" + twob);
        player.getPacketSender().sendString(8428, twob + "2 Bars" + twob);
        player.getPacketSender().sendString(1124, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1125, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1126, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1127, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1128, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1129, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1130, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1131, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(13357, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(11459, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1101, GetForlvl(18, player) + "Plate Body" + GetForlvl(18, player));
        player.getPacketSender().sendString(1099, GetForlvl(16, player) + "Plate Legs" + GetForlvl(16, player));
        player.getPacketSender().sendString(1100, GetForlvl(16, player) + "Plate Skirt" + GetForlvl(16, player));
        player.getPacketSender().sendString(1088, GetForlvl(14, player) + "2 Hand Sword" + GetForlvl(14, player));
        player.getPacketSender().sendString(1105, GetForlvl(12, player) + "Kite Shield" + GetForlvl(12, player));
        player.getPacketSender().sendString(1098, GetForlvl(11, player) + "Chain Body" + GetForlvl(11, player));
        player.getPacketSender().sendString(1092, GetForlvl(10, player) + "Battle Axe" + GetForlvl(10, player));
        player.getPacketSender().sendString(1083, GetForlvl(9, player) + "Warhammer" + GetForlvl(9, player));
        player.getPacketSender().sendString(1104, GetForlvl(8, player) + "Square Shield" + GetForlvl(8, player));
        player.getPacketSender().sendString(1103, GetForlvl(7, player) + "Full Helm" + GetForlvl(7, player));
        player.getPacketSender().sendString(1106, GetForlvl(7, player) + "Throwing Knives" + GetForlvl(7, player));
        player.getPacketSender().sendString(1086, GetForlvl(6, player) + "Long Sword" + GetForlvl(6, player));
        player.getPacketSender().sendString(1087, GetForlvl(5, player) + "Scimitar" + GetForlvl(5, player));
        player.getPacketSender().sendString(1108, GetForlvl(5, player) + "Arrowtips" + GetForlvl(5, player));
        player.getPacketSender().sendString(1085, GetForlvl(4, player) + "Sword" + GetForlvl(4, player));
        player.getPacketSender().sendString(1107, GetForlvl(4, player) + "Dart tips" + GetForlvl(4, player));
        player.getPacketSender().sendString(13358, GetForlvl(4, player) + "Nails" + GetForlvl(4, player));
        player.getPacketSender().sendString(1102, GetForlvl(3, player) + "Medium Helm" + GetForlvl(3, player));
        player.getPacketSender().sendString(1093, GetForlvl(2, player) + "Mace" + GetForlvl(2, player));
        player.getPacketSender().sendString(1094, GetForlvl(1, player) + "Bolts" + GetForlvl(1, player));
        player.getPacketSender().sendString(1091, GetForlvl(1, player) + "Hatchet" + GetForlvl(1, player));
        player.getPacketSender().sendString(8429, GetForlvl(8, player) + "Claws" + GetForlvl(8, player));
        ArrayList<Item> itemsColumnOne = new ArrayList<>();
        itemsColumnOne.add(new Item(877, 1));
        itemsColumnOne.add(new Item(1277, 1));
        itemsColumnOne.add(new Item(1321, 1));
        itemsColumnOne.add(new Item(1291, 1));
        itemsColumnOne.add(new Item(1307, 1));
        player.getPacketSender().sendItemsOnInterface(1119, itemsColumnOne.size(), itemsColumnOne, true);

        /*player.getPacketSender().sendSmithingData(877, 0, 1119, 1);
        player.getPacketSender().sendSmithingData(1277, 0, 1119, 1);
        player.getPacketSender().sendSmithingData(1321, 2, 1119, 1);
        player.getPacketSender().sendSmithingData(1291, 3, 1119, 1);
        player.getPacketSender().sendSmithingData(1307, 4, 1119, 1);*/

        ArrayList<Item> itemsColumnTwo = new ArrayList<>();
        itemsColumnTwo.add(new Item(1351, 1));
        itemsColumnTwo.add(new Item(1422, 1));
        itemsColumnTwo.add(new Item(1337, 1));
        itemsColumnTwo.add(new Item(1375, 1));
        itemsColumnTwo.add(new Item(3095, 1));
        player.getPacketSender().sendItemsOnInterface(1120, itemsColumnTwo.size(), itemsColumnTwo, true);

        /*player.getPacketSender().sendSmithingData(1351, 0, 1120, 1);
        player.getPacketSender().sendSmithingData(1422, 1, 1120, 1);
        player.getPacketSender().sendSmithingData(1337, 2, 1120, 1);
        player.getPacketSender().sendSmithingData(1375, 3, 1120, 1);
        player.getPacketSender().sendSmithingData(3095, 4, 1120, 1);*/

        ArrayList<Item> itemsColumnThree = new ArrayList<>();
        itemsColumnThree.add(new Item(1103, 1));
        itemsColumnThree.add(new Item(1075, 1));
        itemsColumnThree.add(new Item(1087, 1));
        itemsColumnThree.add(new Item(1117, 1));
        player.getPacketSender().sendItemsOnInterface(1121, itemsColumnThree.size(), itemsColumnThree, true);

        /*player.getPacketSender().sendSmithingData(1103, 0, 1121, 1);
        player.getPacketSender().sendSmithingData(1075, 1, 1121, 1);
        player.getPacketSender().sendSmithingData(1087, 2, 1121, 1);
        player.getPacketSender().sendSmithingData(1117, 3, 1121, 1);*/

        ArrayList<Item> itemsColumnFour = new ArrayList<>();
        itemsColumnFour.add(new Item(1139, 1));
        itemsColumnFour.add(new Item(1155, 1));
        itemsColumnFour.add(new Item(1173, 1));
        itemsColumnFour.add(new Item(1189, 1));
        itemsColumnFour.add(new Item(4819, 15));
        player.getPacketSender().sendItemsOnInterface(1122, itemsColumnFour.size(), itemsColumnFour, true);

        /*player.getPacketSender().sendSmithingData(1139, 0, 1122, 1);
        player.getPacketSender().sendSmithingData(1155, 1, 1122, 1);
        player.getPacketSender().sendSmithingData(1173, 2, 1122, 1);
        player.getPacketSender().sendSmithingData(1189, 3, 1122, 1);
        player.getPacketSender().sendSmithingData(4819, 4, 1122, 15);*/

        ArrayList<Item> itemsColumnFive = new ArrayList<>();
        itemsColumnFive.add(new Item(819, 10));
        itemsColumnFive.add(new Item(39, 15));
        itemsColumnFive.add(new Item(864, 5));
        player.getPacketSender().sendItemsOnInterface(1123, itemsColumnFive.size(), itemsColumnFive, true);

        /*player.getPacketSender().sendSmithingData(819, 0, 1123, 10);
        player.getPacketSender().sendSmithingData(39, 1, 1123, 15);
        player.getPacketSender().sendSmithingData(864, 2, 1123, 5);
        player.getPacketSender().sendSmithingData(-1, 4, 1123, 1);*/

        player.getPacketSender().sendString(1135, "");
        player.getPacketSender().sendString(1134, "");
        player.getPacketSender().sendString(11461, "");
        player.getPacketSender().sendString(11459, "");
        player.getPacketSender().sendString(1132, "");
        player.getPacketSender().sendString(1096, "");
        player.getPacketSender().sendInterface(994);
    }

    public static void makeIronInterface(Player player) {
        String fiveb = GetForBars(2351, 5, player);
        String threeb = GetForBars(2351, 3, player);
        String twob = GetForBars(2351, 2, player);
        String oneb = GetForBars(2351, 1, player);
        player.getPacketSender().sendString(1112, fiveb + "5 Bars" + fiveb);
        player.getPacketSender().sendString(1109, threeb + "3 Bars" + threeb);
        player.getPacketSender().sendString(1110, threeb + "3 Bars" + threeb);
        player.getPacketSender().sendString(1118, threeb + "3 Bars" + threeb);
        player.getPacketSender().sendString(1111, threeb + "3 Bars" + threeb);
        player.getPacketSender().sendString(1095, threeb + "3 Bars" + threeb);
        player.getPacketSender().sendString(1115, threeb + "3 Bars" + threeb);
        player.getPacketSender().sendString(1090, threeb + "3 Bars" + threeb);
        player.getPacketSender().sendString(1113, twob + "2 Bars" + twob);
        player.getPacketSender().sendString(1116, twob + "2 Bars" + twob);
        player.getPacketSender().sendString(1114, twob + "2 Bars" + twob);
        player.getPacketSender().sendString(1089, twob + "2 Bars" + twob);
        player.getPacketSender().sendString(8428, twob + "2 Bars" + twob);
        player.getPacketSender().sendString(1124, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1125, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1126, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1127, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1128, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1129, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1130, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1131, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(13357, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(11459, oneb + "1 Bar" + oneb);
        player.getPacketSender().sendString(1101, GetForlvl(33, player) + "Plate Body" + GetForlvl(18, player));
        player.getPacketSender().sendString(1099, GetForlvl(31, player) + "Plate Legs" + GetForlvl(16, player));
        player.getPacketSender().sendString(1100, GetForlvl(31, player) + "Plate Skirt" + GetForlvl(16, player));
        sendString(player, (GetForlvl(29, player) + "2 Hand Sword" + GetForlvl(14, player)), 1088);
        sendString(player, (GetForlvl(27, player) + "Kite Shield" + GetForlvl(12, player)), 1105);
        sendString(player, (GetForlvl(26, player) + "Chain Body" + GetForlvl(11, player)), 1098);
        sendString(player, (GetForlvl(26, player) + "Oil Lantern Frame" + GetForlvl(11, player)), 11461);
        sendString(player, (GetForlvl(25, player) + "Battle Axe" + GetForlvl(10, player)), 1092);
        sendString(player, (GetForlvl(24, player) + "Warhammer" + GetForlvl(9, player)), 1083);
        sendString(player, (GetForlvl(23, player) + "Square Shield" + GetForlvl(8, player)), 1104);
        sendString(player, (GetForlvl(22, player) + "Full Helm" + GetForlvl(7, player)), 1103);
        sendString(player, (GetForlvl(21, player) + "Throwing Knives" + GetForlvl(7, player)), 1106);
        sendString(player, (GetForlvl(21, player) + "Long Sword" + GetForlvl(6, player)), 1086);
        sendString(player, (GetForlvl(20, player) + "Scimitar" + GetForlvl(5, player)), 1087);
        sendString(player, (GetForlvl(20, player) + "Arrowtips" + GetForlvl(5, player)), 1108);
        sendString(player, (GetForlvl(19, player) + "Sword" + GetForlvl(4, player)), 1085);
        sendString(player, (GetForlvl(19, player) + "Dart tips" + GetForlvl(4, player)), 9140);
        sendString(player, (GetForlvl(19, player) + "Nails" + GetForlvl(4, player)), 13358);
        sendString(player, (GetForlvl(18, player) + "Medium Helm" + GetForlvl(3, player)), 1102);
        sendString(player, (GetForlvl(17, player) + "Mace" + GetForlvl(2, player)), 1093);
        sendString(player, (GetForlvl(15, player) + "Bolts" + GetForlvl(1, player)), 1094);
        sendString(player, (GetForlvl(16, player) + "Axe" + GetForlvl(1, player)), 1091);
        ArrayList<Item> itemsColumnOne = new ArrayList<>();
        itemsColumnOne.add(new Item(9140, 1));
        itemsColumnOne.add(new Item(1279, 1));
        itemsColumnOne.add(new Item(1323, 1));
        itemsColumnOne.add(new Item(1293, 1));
        itemsColumnOne.add(new Item(1309, 1));
        player.getPacketSender().sendItemsOnInterface(1119, itemsColumnOne.size(), itemsColumnOne, true);

        /*player.getPacketSender().sendSmithingData(877, 0, 1119, 1);
        player.getPacketSender().sendSmithingData(1277, 0, 1119, 1);
        player.getPacketSender().sendSmithingData(1321, 2, 1119, 1);
        player.getPacketSender().sendSmithingData(1291, 3, 1119, 1);
        player.getPacketSender().sendSmithingData(1307, 4, 1119, 1);*/

        ArrayList<Item> itemsColumnTwo = new ArrayList<>();
        itemsColumnTwo.add(new Item(1349, 1));
        itemsColumnTwo.add(new Item(1420, 1));
        itemsColumnTwo.add(new Item(1335, 1));
        itemsColumnTwo.add(new Item(1363, 1));
        itemsColumnTwo.add(new Item(3096, 1));
        player.getPacketSender().sendItemsOnInterface(1120, itemsColumnTwo.size(), itemsColumnTwo, true);

        /*player.getPacketSender().sendSmithingData(1351, 0, 1120, 1);
        player.getPacketSender().sendSmithingData(1422, 1, 1120, 1);
        player.getPacketSender().sendSmithingData(1337, 2, 1120, 1);
        player.getPacketSender().sendSmithingData(1375, 3, 1120, 1);
        player.getPacketSender().sendSmithingData(3095, 4, 1120, 1);*/

        ArrayList<Item> itemsColumnThree = new ArrayList<>();
        itemsColumnThree.add(new Item(1101, 1));
        itemsColumnThree.add(new Item(1067, 1));
        itemsColumnThree.add(new Item(1081, 1));
        itemsColumnThree.add(new Item(1115, 1));
        itemsColumnThree.add(new Item(4540, 1));		
        player.getPacketSender().sendItemsOnInterface(1121, itemsColumnThree.size(), itemsColumnThree, true);

        /*player.getPacketSender().sendSmithingData(1103, 0, 1121, 1);
        player.getPacketSender().sendSmithingData(1075, 1, 1121, 1);
        player.getPacketSender().sendSmithingData(1087, 2, 1121, 1);
        player.getPacketSender().sendSmithingData(1117, 3, 1121, 1);*/

        ArrayList<Item> itemsColumnFour = new ArrayList<>();
        itemsColumnFour.add(new Item(1137, 1));
        itemsColumnFour.add(new Item(1153, 1));
        itemsColumnFour.add(new Item(1175, 1));
        itemsColumnFour.add(new Item(1191, 1));
        itemsColumnFour.add(new Item(4820, 15));
        player.getPacketSender().sendItemsOnInterface(1122, itemsColumnFour.size(), itemsColumnFour, true);

        /*player.getPacketSender().sendSmithingData(1139, 0, 1122, 1);
        player.getPacketSender().sendSmithingData(1155, 1, 1122, 1);
        player.getPacketSender().sendSmithingData(1173, 2, 1122, 1);
        player.getPacketSender().sendSmithingData(1189, 3, 1122, 1);
        player.getPacketSender().sendSmithingData(4819, 4, 1122, 15);*/

        ArrayList<Item> itemsColumnFive = new ArrayList<>();
        itemsColumnFive.add(new Item(820, 10));
        itemsColumnFive.add(new Item(40, 15));
        itemsColumnFive.add(new Item(863, 5));
        player.getPacketSender().sendItemsOnInterface(1123, itemsColumnFive.size(), itemsColumnFive, true);		
        sendString(player, "", 1135);
        sendString(player, "", 1134);
        sendString(player, "", 11461);
        sendString(player, "", 11459);
        sendString(player, "", 1132);
        sendString(player, "", 1096);
        player.getPacketSender().sendInterface(994);
    }

    public static void makeSteelInterface(Player player) {
        String fiveb = GetForBars(2353, 5, player);
        String threeb = GetForBars(2353, 3, player);
        String twob = GetForBars(2353, 2, player);
        String oneb = GetForBars(2353, 1, player);
        sendString(player, fiveb + "5 Bars" + fiveb, 1112);
        sendString(player, threeb + "3 Bars" + threeb, 1109);
        sendString(player, threeb + "3 Bars" + threeb, 1110);
        sendString(player, threeb + "3 Bars" + threeb, 1118);
        sendString(player, threeb + "3 Bars" + threeb, 1111);
        sendString(player, threeb + "3 Bars" + threeb, 1095);
        sendString(player, threeb + "3 Bars" + threeb, 1115);
        sendString(player, threeb + "3 Bars" + threeb, 1090);
        sendString(player, twob + "2 Bars" + twob, 1113);
        sendString(player, twob + "2 Bars" + twob, 1116);
        sendString(player, twob + "2 Bars" + twob, 1114);
        sendString(player, twob + "2 Bars" + twob, 1089);
        sendString(player, twob + "2 Bars" + twob, 8428);
        sendString(player, oneb + "1 Bar" + oneb, 1124);
        sendString(player, oneb + "1 Bar" + oneb, 1125);
        sendString(player, oneb + "1 Bar" + oneb, 1126);
        sendString(player, oneb + "1 Bar" + oneb, 1127);
        sendString(player, oneb + "1 Bar" + oneb, 1128);
        sendString(player, oneb + "1 Bar" + oneb, 1129);
        sendString(player, oneb + "1 Bar" + oneb, 1130);
        sendString(player, oneb + "1 Bar" + oneb, 1131);
        sendString(player, oneb + "1 Bar" + oneb, 13357);
        sendString(player, oneb + "1 Bar" + oneb, 1132);
        sendString(player, oneb + "1 Bar" + oneb, 1135);
        sendString(player, "", 11459);
        sendString(player, GetForlvl(48, player) + "Plate Body" + GetForlvl(18, player), 1101);
        sendString(player, GetForlvl(46, player) + "Plate Legs" + GetForlvl(16, player), 1099);
        sendString(player, GetForlvl(46, player) + "Plate Skirt" + GetForlvl(16, player), 1100);
        sendString(player, GetForlvl(44, player) + "2 Hand Sword" + GetForlvl(14, player), 1088);
        sendString(player, GetForlvl(42, player) + "Kite Shield" + GetForlvl(12, player), 1105);
        sendString(player, GetForlvl(41, player) + "Chain Body" + GetForlvl(11, player), 1098);
        sendString(player, "", 11461);
        sendString(player, GetForlvl(40, player) + "Battle Axe" + GetForlvl(10, player), 1092);
        sendString(player, GetForlvl(39, player) + "Warhammer" + GetForlvl(9, player), 1083);
        sendString(player, GetForlvl(38, player) + "Square Shield" + GetForlvl(8, player), 1104);
        sendString(player, GetForlvl(37, player) + "Full Helm" + GetForlvl(7, player), 1103);
        sendString(player, GetForlvl(37, player) + "Throwing Knives" + GetForlvl(7, player), 1106);
        sendString(player, GetForlvl(36, player) + "Long Sword" + GetForlvl(6, player), 1086);
        sendString(player, GetForlvl(35, player) + "Scimitar" + GetForlvl(5, player), 1087);
        sendString(player, GetForlvl(35, player) + "Arrowtips" + GetForlvl(5, player), 1108);
        sendString(player, GetForlvl(34, player) + "Sword" + GetForlvl(4, player), 1085);
        sendString(player, GetForlvl(34, player) + "Dart tips" + GetForlvl(4, player), 9141);
        sendString(player, GetForlvl(34, player) + "Nails" + GetForlvl(4, player), 13358);
        sendString(player, GetForlvl(33, player) + "Medium Helm" + GetForlvl(3, player), 1102);
        sendString(player, GetForlvl(32, player) + "Mace" + GetForlvl(2, player), 1093);
        sendString(player, GetForlvl(30, player) + "Bolts" + GetForlvl(1, player), 1094);
        sendString(player, GetForlvl(31, player) + "Axe" + GetForlvl(1, player), 1091);
        sendString(player, GetForlvl(35, player) + "Cannon Ball" + GetForlvl(35, player), 1096);
        sendString(player, GetForlvl(36, player) + "Studs" + GetForlvl(36, player), 1134);
        ArrayList<Item> itemsColumnOne = new ArrayList<>();
        itemsColumnOne.add(new Item(9141, 1));
        itemsColumnOne.add(new Item(1281, 1));
        itemsColumnOne.add(new Item(1325, 1));
        itemsColumnOne.add(new Item(1295, 1));
        itemsColumnOne.add(new Item(1311, 1));
        player.getPacketSender().sendItemsOnInterface(1119, itemsColumnOne.size(), itemsColumnOne, true);

        /*player.getPacketSender().sendSmithingData(877, 0, 1119, 1);
        player.getPacketSender().sendSmithingData(1277, 0, 1119, 1);
        player.getPacketSender().sendSmithingData(1321, 2, 1119, 1);
        player.getPacketSender().sendSmithingData(1291, 3, 1119, 1);
        player.getPacketSender().sendSmithingData(1307, 4, 1119, 1);*/

        ArrayList<Item> itemsColumnTwo = new ArrayList<>();
        itemsColumnTwo.add(new Item(1353, 1));
        itemsColumnTwo.add(new Item(1424, 1));
        itemsColumnTwo.add(new Item(1339, 1));
        itemsColumnTwo.add(new Item(1365, 1));
        itemsColumnTwo.add(new Item(3097, 1));
        player.getPacketSender().sendItemsOnInterface(1120, itemsColumnTwo.size(), itemsColumnTwo, true);

        /*player.getPacketSender().sendSmithingData(1351, 0, 1120, 1);
        player.getPacketSender().sendSmithingData(1422, 1, 1120, 1);
        player.getPacketSender().sendSmithingData(1337, 2, 1120, 1);
        player.getPacketSender().sendSmithingData(1375, 3, 1120, 1);
        player.getPacketSender().sendSmithingData(3095, 4, 1120, 1);*/

        ArrayList<Item> itemsColumnThree = new ArrayList<>();
        itemsColumnThree.add(new Item(1105, 1));
        itemsColumnThree.add(new Item(1069, 1));
        itemsColumnThree.add(new Item(1083, 1));
        itemsColumnThree.add(new Item(1119, 1));
        player.getPacketSender().sendItemsOnInterface(1121, itemsColumnThree.size(), itemsColumnThree, true);

        /*player.getPacketSender().sendSmithingData(1103, 0, 1121, 1);
        player.getPacketSender().sendSmithingData(1075, 1, 1121, 1);
        player.getPacketSender().sendSmithingData(1087, 2, 1121, 1);
        player.getPacketSender().sendSmithingData(1117, 3, 1121, 1);*/

        ArrayList<Item> itemsColumnFour = new ArrayList<>();
        itemsColumnFour.add(new Item(1141, 1));
        itemsColumnFour.add(new Item(1157, 1));
        itemsColumnFour.add(new Item(1177, 1));
        itemsColumnFour.add(new Item(1193, 1));
        itemsColumnFour.add(new Item(1539, 15));
        player.getPacketSender().sendItemsOnInterface(1122, itemsColumnFour.size(), itemsColumnFour, true);

        /*player.getPacketSender().sendSmithingData(1139, 0, 1122, 1);
        player.getPacketSender().sendSmithingData(1155, 1, 1122, 1);
        player.getPacketSender().sendSmithingData(1173, 2, 1122, 1);
        player.getPacketSender().sendSmithingData(1189, 3, 1122, 1);
        player.getPacketSender().sendSmithingData(4819, 4, 1122, 15);*/

        ArrayList<Item> itemsColumnFive = new ArrayList<>();
        itemsColumnFive.add(new Item(821, 10));
        itemsColumnFive.add(new Item(41, 15));
        itemsColumnFive.add(new Item(865, 5));
        itemsColumnFive.add(new Item(2, 15));
        itemsColumnFive.add(new Item(2370, 5));		
        player.getPacketSender().sendItemsOnInterface(1123, itemsColumnFive.size(), itemsColumnFive, true);				
        sendString(player, "", 1135);
        sendString(player, "", 1134);
        sendString(player, "", 11461);
        sendString(player, "", 11459);
        sendString(player, "", 1132);
        sendString(player, "", 1096);
        player.getPacketSender().sendInterface(994);
    }

    public static void makeMithInterface(Player player) {
        String fiveb = GetForBars(2359, 5, player);
        String threeb = GetForBars(2359, 3, player);
        String twob = GetForBars(2359, 2, player);
        String oneb = GetForBars(2359, 1, player);
        sendString(player, fiveb + "5 Bars" + fiveb, 1112);
        sendString(player, threeb + "3 Bars" + threeb, 1109);
        sendString(player, threeb + "3 Bars" + threeb, 1110);
        sendString(player, threeb + "3 Bars" + threeb, 1118);
        sendString(player, threeb + "3 Bars" + threeb, 1111);
        sendString(player, threeb + "3 Bars" + threeb, 1095);
        sendString(player, threeb + "3 Bars" + threeb, 1115);
        sendString(player, threeb + "3 Bars" + threeb, 1090);
        sendString(player, twob + "2 Bars" + twob, 1113);
        sendString(player, twob + "2 Bars" + twob, 1116);
        sendString(player, twob + "2 Bars" + twob, 1114);
        sendString(player, twob + "2 Bars" + twob, 1089);
        sendString(player, twob + "2 Bars" + twob, 8428);
        sendString(player, oneb + "1 Bar" + oneb, 1124);
        sendString(player, oneb + "1 Bar" + oneb, 1125);
        sendString(player, oneb + "1 Bar" + oneb, 1126);
        sendString(player, oneb + "1 Bar" + oneb, 1127);
        sendString(player, oneb + "1 Bar" + oneb, 1128);
        sendString(player, oneb + "1 Bar" + oneb, 1129);
        sendString(player, oneb + "1 Bar" + oneb, 1130);
        sendString(player, oneb + "1 Bar" + oneb, 1131);
        sendString(player, oneb + "1 Bar" + oneb, 13357);
        sendString(player, oneb + "1 Bar" + oneb, 11459);
        sendString(player, GetForlvl(68, player) + "Plate Body" + GetForlvl(18, player), 1101);
        sendString(player, GetForlvl(66, player) + "Plate Legs" + GetForlvl(16, player), 1099);
        sendString(player, GetForlvl(66, player) + "Plate Skirt" + GetForlvl(16, player), 1100);
        sendString(player, GetForlvl(64, player) + "2 Hand Sword" + GetForlvl(14, player), 1088);
        sendString(player, GetForlvl(62, player) + "Kite Shield" + GetForlvl(12, player), 1105);
        sendString(player, GetForlvl(61, player) + "Chain Body" + GetForlvl(11, player), 1098);
        sendString(player, GetForlvl(60, player) + "Battle Axe" + GetForlvl(10, player), 1092);
        sendString(player, GetForlvl(59, player) + "Warhammer" + GetForlvl(9, player), 1083);
        sendString(player, GetForlvl(58, player) + "Square Shield" + GetForlvl(8, player), 1104);
        sendString(player, GetForlvl(57, player) + "Full Helm" + GetForlvl(7, player), 1103);
        sendString(player, GetForlvl(57, player) + "Throwing Knives" + GetForlvl(7, player), 1106);
        sendString(player, GetForlvl(56, player) + "Long Sword" + GetForlvl(6, player), 1086);
        sendString(player, GetForlvl(55, player) + "Scimitar" + GetForlvl(5, player), 1087);
        sendString(player, GetForlvl(55, player) + "Arrowtips" + GetForlvl(5, player), 1108);
        sendString(player, GetForlvl(54, player) + "Sword" + GetForlvl(4, player), 1085);
        sendString(player, GetForlvl(54, player) + "Dart tips" + GetForlvl(4, player), 9142);
        sendString(player, GetForlvl(54, player) + "Nails" + GetForlvl(4, player), 13358);
        sendString(player, GetForlvl(53, player) + "Medium Helm" + GetForlvl(3, player), 1102);
        sendString(player, GetForlvl(52, player) + "Mace" + GetForlvl(2, player), 1093);
        sendString(player, GetForlvl(50, player) + "Bolts" + GetForlvl(1, player), 1094);
        sendString(player, GetForlvl(51, player) + "Axe" + GetForlvl(1, player), 1091);
        player.getPacketSender().sendSmithingData(9142, 0, 1119, 10); //dagger
        player.getPacketSender().sendSmithingData(1355, 0, 1120, 1); //axe
        player.getPacketSender().sendSmithingData(1109, 0, 1121, 1); //chain body
        player.getPacketSender().sendSmithingData(1143, 0, 1122, 1); //med helm
        player.getPacketSender().sendSmithingData(822, 0, 1123, 10); //Bolts
        player.getPacketSender().sendSmithingData(1285, 1, 1119, 1); //s-sword
        player.getPacketSender().sendSmithingData(1428, 1, 1120, 1); //mace
        player.getPacketSender().sendSmithingData(1071, 1, 1121, 1); //platelegs
        player.getPacketSender().sendSmithingData(1159, 1, 1122, 1); //full helm
        player.getPacketSender().sendSmithingData(42, 1, 1123, 15); //arrowtips
        player.getPacketSender().sendSmithingData(1329, 2, 1119, 1); //scimmy
        player.getPacketSender().sendSmithingData(1343, 2, 1120, 1); //warhammer
        player.getPacketSender().sendSmithingData(1085, 2, 1121, 1); //plateskirt
        player.getPacketSender().sendSmithingData(1181, 2, 1122, 1); //Sq. Shield
        player.getPacketSender().sendSmithingData(866, 2, 1123, 5); //throwing-knives
        player.getPacketSender().sendSmithingData(1299, 3, 1119, 1); //longsword
        player.getPacketSender().sendSmithingData(1369, 3, 1120, 1); //battleaxe
        player.getPacketSender().sendSmithingData(1121, 3, 1121, 1); //platebody
        player.getPacketSender().sendSmithingData(1197, 3, 1122, 1); //kiteshield
        player.getPacketSender().sendSmithingData(1315, 4, 1119, 1); //2h sword
        player.getPacketSender().sendSmithingData(4822, 4, 1122, 15); //nails
        player.getPacketSender().sendSmithingData(3099, 4, 1120, 1);
        player.getPacketSender().sendSmithingData(-1, 3, 1123, 1);
        ArrayList<Item> itemsColumnOne = new ArrayList<>();
        itemsColumnOne.add(new Item(9142, 1));
        itemsColumnOne.add(new Item(1285, 1));
        itemsColumnOne.add(new Item(1329, 1));
        itemsColumnOne.add(new Item(1299, 1));
        itemsColumnOne.add(new Item(1315, 1));
        player.getPacketSender().sendItemsOnInterface(1119, itemsColumnOne.size(), itemsColumnOne, true);

        /*player.getPacketSender().sendSmithingData(877, 0, 1119, 1);
        player.getPacketSender().sendSmithingData(1277, 0, 1119, 1);
        player.getPacketSender().sendSmithingData(1321, 2, 1119, 1);
        player.getPacketSender().sendSmithingData(1291, 3, 1119, 1);
        player.getPacketSender().sendSmithingData(1307, 4, 1119, 1);*/

        ArrayList<Item> itemsColumnTwo = new ArrayList<>();
        itemsColumnTwo.add(new Item(1355, 1));
        itemsColumnTwo.add(new Item(1428, 1));
        itemsColumnTwo.add(new Item(1343, 1));
        itemsColumnTwo.add(new Item(1369, 1));
        itemsColumnTwo.add(new Item(3099, 1));
        player.getPacketSender().sendItemsOnInterface(1120, itemsColumnTwo.size(), itemsColumnTwo, true);

        /*player.getPacketSender().sendSmithingData(1351, 0, 1120, 1);
        player.getPacketSender().sendSmithingData(1422, 1, 1120, 1);
        player.getPacketSender().sendSmithingData(1337, 2, 1120, 1);
        player.getPacketSender().sendSmithingData(1375, 3, 1120, 1);
        player.getPacketSender().sendSmithingData(3095, 4, 1120, 1);*/

        ArrayList<Item> itemsColumnThree = new ArrayList<>();
        itemsColumnThree.add(new Item(1109, 1));
        itemsColumnThree.add(new Item(1071, 1));
        itemsColumnThree.add(new Item(1085, 1));
        itemsColumnThree.add(new Item(1121, 1));
        player.getPacketSender().sendItemsOnInterface(1121, itemsColumnThree.size(), itemsColumnThree, true);

        /*player.getPacketSender().sendSmithingData(1103, 0, 1121, 1);
        player.getPacketSender().sendSmithingData(1075, 1, 1121, 1);
        player.getPacketSender().sendSmithingData(1087, 2, 1121, 1);
        player.getPacketSender().sendSmithingData(1117, 3, 1121, 1);*/

        ArrayList<Item> itemsColumnFour = new ArrayList<>();
        itemsColumnFour.add(new Item(1143, 1));
        itemsColumnFour.add(new Item(1159, 1));
        itemsColumnFour.add(new Item(1181, 1));
        itemsColumnFour.add(new Item(1197, 1));
        itemsColumnFour.add(new Item(4822, 15));
        player.getPacketSender().sendItemsOnInterface(1122, itemsColumnFour.size(), itemsColumnFour, true);

        /*player.getPacketSender().sendSmithingData(1139, 0, 1122, 1);
        player.getPacketSender().sendSmithingData(1155, 1, 1122, 1);
        player.getPacketSender().sendSmithingData(1173, 2, 1122, 1);
        player.getPacketSender().sendSmithingData(1189, 3, 1122, 1);
        player.getPacketSender().sendSmithingData(4819, 4, 1122, 15);*/

        ArrayList<Item> itemsColumnFive = new ArrayList<>();
        itemsColumnFive.add(new Item(822, 10));
        itemsColumnFive.add(new Item(42, 15));
        itemsColumnFive.add(new Item(866, 5));		
        player.getPacketSender().sendItemsOnInterface(1123, itemsColumnFive.size(), itemsColumnFive, true);			
        sendString(player, "", 1135);
        sendString(player, "", 1134);
        sendString(player, "", 11461);
        sendString(player, "", 11459);
        sendString(player, "", 1132);
        sendString(player, "", 1096);
        player.getPacketSender().sendInterface(994);
    }

    public static void makeAddyInterface(Player player) {
        String fiveb = GetForBars(2361, 5, player);
        String threeb = GetForBars(2361, 3, player);
        String twob = GetForBars(2361, 2, player);
        String oneb = GetForBars(2361, 1, player);
        sendString(player, fiveb + "5 Bars" + fiveb, 1112);
        sendString(player, threeb + "3 Bars" + threeb, 1109);
        sendString(player, threeb + "3 Bars" + threeb, 1110);
        sendString(player, threeb + "3 Bars" + threeb, 1118);
        sendString(player, threeb + "3 Bars" + threeb, 1111);
        sendString(player, threeb + "3 Bars" + threeb, 1095);
        sendString(player, threeb + "3 Bars" + threeb, 1115);
        sendString(player, threeb + "3 Bars" + threeb, 1090);
        sendString(player, twob + "2 Bars" + twob, 1113);
        sendString(player, twob + "2 Bars" + twob, 1116);
        sendString(player, twob + "2 Bars" + twob, 1114);
        sendString(player, twob + "2 Bars" + twob, 1089);
        sendString(player, twob + "2 Bars" + twob, 8428);
        sendString(player, oneb + "1 Bar" + oneb, 1124);
        sendString(player, oneb + "1 Bar" + oneb, 1125);
        sendString(player, oneb + "1 Bar" + oneb, 1126);
        sendString(player, oneb + "1 Bar" + oneb, 1127);
        sendString(player, oneb + "1 Bar" + oneb, 1128);
        sendString(player, oneb + "1 Bar" + oneb, 1129);
        sendString(player, oneb + "1 Bar" + oneb, 1130);
        sendString(player, oneb + "1 Bar" + oneb, 1131);
        sendString(player, oneb + "1 Bar" + oneb, 13357);
        sendString(player, oneb + "1 Bar" + oneb, 11459);
        sendString(player, GetForlvl(88, player) + "Plate Body" + GetForlvl(18, player), 1101);
        sendString(player, GetForlvl(86, player) + "Plate Legs" + GetForlvl(16, player), 1099);
        sendString(player, GetForlvl(86, player) + "Plate Skirt" + GetForlvl(16, player), 1100);
        sendString(player, GetForlvl(84, player) + "2 Hand Sword" + GetForlvl(14, player), 1088);
        sendString(player, GetForlvl(82, player) + "Kite Shield" + GetForlvl(12, player), 1105);
        sendString(player, GetForlvl(81, player) + "Chain Body" + GetForlvl(11, player), 1098);
        sendString(player, GetForlvl(80, player) + "Battle Axe" + GetForlvl(10, player), 1092);
        sendString(player, GetForlvl(79, player) + "Warhammer" + GetForlvl(9, player), 1083);
        sendString(player, GetForlvl(78, player) + "Square Shield" + GetForlvl(8, player), 1104);
        sendString(player, GetForlvl(77, player) + "Full Helm" + GetForlvl(7, player), 1103);
        sendString(player, GetForlvl(77, player) + "Throwing Knives" + GetForlvl(7, player), 1106);
        sendString(player, GetForlvl(76, player) + "Long Sword" + GetForlvl(6, player), 1086);
        sendString(player, GetForlvl(75, player) + "Scimitar" + GetForlvl(5, player), 1087);
        sendString(player, GetForlvl(75, player) + "Arrowtips" + GetForlvl(5, player), 1108);
        sendString(player, GetForlvl(74, player) + "Sword" + GetForlvl(4, player), 1085);
        sendString(player, GetForlvl(74, player) + "Dart tips" + GetForlvl(4, player), 823);
        sendString(player, GetForlvl(74, player) + "Nails" + GetForlvl(4, player), 13358);
        sendString(player, GetForlvl(73, player) + "Medium Helm" + GetForlvl(3, player), 1102);
        sendString(player, GetForlvl(72, player) + "Mace" + GetForlvl(2, player), 1093);
        sendString(player, GetForlvl(70, player) + "Bolts" + GetForlvl(1, player), 1094);
        sendString(player, GetForlvl(71, player) + "Axe" + GetForlvl(1, player), 1091);
        player.getPacketSender().sendSmithingData(9143, 0, 1119, 10); //dagger
        player.getPacketSender().sendSmithingData(1357, 0, 1120, 1); //axe
        player.getPacketSender().sendSmithingData(1111, 0, 1121, 1); //chain body
        player.getPacketSender().sendSmithingData(1145, 0, 1122, 1); //med helm
        player.getPacketSender().sendSmithingData(823, 0, 1123, 10); //Bolts
        player.getPacketSender().sendSmithingData(1287, 1, 1119, 1); //s-sword
        player.getPacketSender().sendSmithingData(1430, 1, 1120, 1); //mace
        player.getPacketSender().sendSmithingData(1073, 1, 1121, 1); //platelegs
        player.getPacketSender().sendSmithingData(1161, 1, 1122, 1); //full helm
        player.getPacketSender().sendSmithingData(43, 1, 1123, 15); //arrowtips
        player.getPacketSender().sendSmithingData(1331, 2, 1119, 1); //scimmy
        player.getPacketSender().sendSmithingData(1345, 2, 1120, 1); //warhammer
        player.getPacketSender().sendSmithingData(1091, 2, 1121, 1); //plateskirt
        player.getPacketSender().sendSmithingData(1183, 2, 1122, 1); //Sq. Shield
        player.getPacketSender().sendSmithingData(867, 2, 1123, 5); //throwing-knives
        player.getPacketSender().sendSmithingData(1301, 3, 1119, 1); //longsword
        player.getPacketSender().sendSmithingData(1371, 3, 1120, 1); //battleaxe
        player.getPacketSender().sendSmithingData(1123, 3, 1121, 1); //platebody
        player.getPacketSender().sendSmithingData(1199, 3, 1122, 1); //kiteshield
        player.getPacketSender().sendSmithingData(1317, 4, 1119, 1); //2h sword
        player.getPacketSender().sendSmithingData(4823, 4, 1122, 15); //nails
        player.getPacketSender().sendSmithingData(3100, 4, 1120, 1); // claws
        player.getPacketSender().sendSmithingData(-1, 3, 1123, 1);
        ArrayList<Item> itemsColumnOne = new ArrayList<>();
        itemsColumnOne.add(new Item(9143, 1));
        itemsColumnOne.add(new Item(1287, 1));
        itemsColumnOne.add(new Item(1331, 1));
        itemsColumnOne.add(new Item(1301, 1));
        itemsColumnOne.add(new Item(1317, 1));
        player.getPacketSender().sendItemsOnInterface(1119, itemsColumnOne.size(), itemsColumnOne, true);

        /*player.getPacketSender().sendSmithingData(877, 0, 1119, 1);
        player.getPacketSender().sendSmithingData(1277, 0, 1119, 1);
        player.getPacketSender().sendSmithingData(1321, 2, 1119, 1);
        player.getPacketSender().sendSmithingData(1291, 3, 1119, 1);
        player.getPacketSender().sendSmithingData(1307, 4, 1119, 1);*/

        ArrayList<Item> itemsColumnTwo = new ArrayList<>();
        itemsColumnTwo.add(new Item(1357, 1));
        itemsColumnTwo.add(new Item(1430, 1));
        itemsColumnTwo.add(new Item(1345, 1));
        itemsColumnTwo.add(new Item(1371, 1));
        itemsColumnTwo.add(new Item(3100, 1));
        player.getPacketSender().sendItemsOnInterface(1120, itemsColumnTwo.size(), itemsColumnTwo, true);

        /*player.getPacketSender().sendSmithingData(1351, 0, 1120, 1);
        player.getPacketSender().sendSmithingData(1422, 1, 1120, 1);
        player.getPacketSender().sendSmithingData(1337, 2, 1120, 1);
        player.getPacketSender().sendSmithingData(1375, 3, 1120, 1);
        player.getPacketSender().sendSmithingData(3095, 4, 1120, 1);*/

        ArrayList<Item> itemsColumnThree = new ArrayList<>();
        itemsColumnThree.add(new Item(1111, 1));
        itemsColumnThree.add(new Item(1073, 1));
        itemsColumnThree.add(new Item(1091, 1));
        itemsColumnThree.add(new Item(1123, 1));
        player.getPacketSender().sendItemsOnInterface(1121, itemsColumnThree.size(), itemsColumnThree, true);

        /*player.getPacketSender().sendSmithingData(1103, 0, 1121, 1);
        player.getPacketSender().sendSmithingData(1075, 1, 1121, 1);
        player.getPacketSender().sendSmithingData(1087, 2, 1121, 1);
        player.getPacketSender().sendSmithingData(1117, 3, 1121, 1);*/

        ArrayList<Item> itemsColumnFour = new ArrayList<>();
        itemsColumnFour.add(new Item(1145, 1));
        itemsColumnFour.add(new Item(1161, 1));
        itemsColumnFour.add(new Item(1183, 1));
        itemsColumnFour.add(new Item(1199, 1));
        itemsColumnFour.add(new Item(4823, 15));
        player.getPacketSender().sendItemsOnInterface(1122, itemsColumnFour.size(), itemsColumnFour, true);

        /*player.getPacketSender().sendSmithingData(1139, 0, 1122, 1);
        player.getPacketSender().sendSmithingData(1155, 1, 1122, 1);
        player.getPacketSender().sendSmithingData(1173, 2, 1122, 1);
        player.getPacketSender().sendSmithingData(1189, 3, 1122, 1);
        player.getPacketSender().sendSmithingData(4819, 4, 1122, 15);*/

        ArrayList<Item> itemsColumnFive = new ArrayList<>();
        itemsColumnFive.add(new Item(823, 10));
        itemsColumnFive.add(new Item(43, 15));
        itemsColumnFive.add(new Item(867, 5));		
        player.getPacketSender().sendItemsOnInterface(1123, itemsColumnFive.size(), itemsColumnFive, true);					
        sendString(player, "", 1135);
        sendString(player, "", 1134);
        sendString(player, "", 11461);
        sendString(player, "", 11459);
        sendString(player, "", 1132);
        sendString(player, "", 1096);
        player.getPacketSender().sendInterface(994);
    }

    public static void makeRuneInterface(Player player) {
        String fiveb = GetForBars(2363, 5, player);
        String threeb = GetForBars(2363, 3, player);
        String twob = GetForBars(2363, 2, player);
        String oneb = GetForBars(2363, 1, player);
        sendString(player, fiveb + "5 Bars" + fiveb, 1112);
        sendString(player, threeb + "3 Bars" + threeb, 1109);
        sendString(player, threeb + "3 Bars" + threeb, 1110);
        sendString(player, threeb + "3 Bars" + threeb, 1118);
        sendString(player, threeb + "3 Bars" + threeb, 1111);
        sendString(player, threeb + "3 Bars" + threeb, 1095);
        sendString(player, threeb + "3 Bars" + threeb, 1115);
        sendString(player, threeb + "3 Bars" + threeb, 1090);
        sendString(player, twob + "2 Bars" + twob, 1113);
        sendString(player, twob + "2 Bars" + twob, 1116);
        sendString(player, twob + "2 Bars" + twob, 1114);
        sendString(player, twob + "2 Bars" + twob, 1089);
        sendString(player, twob + "2 Bars" + twob, 8428);
        sendString(player, oneb + "1 Bar" + oneb, 1124);
        sendString(player, oneb + "1 Bar" + oneb, 1125);
        sendString(player, oneb + "1 Bar" + oneb, 1126);
        sendString(player, oneb + "1 Bar" + oneb, 1127);
        sendString(player, oneb + "1 Bar" + oneb, 1128);
        sendString(player, oneb + "1 Bar" + oneb, 1129);
        sendString(player, oneb + "1 Bar" + oneb, 1130);
        sendString(player, oneb + "1 Bar" + oneb, 1131);
        sendString(player, oneb + "1 Bar" + oneb, 13357);
        sendString(player, oneb + "1 Bar" + oneb, 11459);
        sendString(player, GetForlvl(88, player) + "Plate Body" + GetForlvl(18, player), 1101);
        sendString(player, GetForlvl(99, player) + "Plate Legs" + GetForlvl(16, player), 1099);
        sendString(player, GetForlvl(99, player) + "Plate Skirt" + GetForlvl(16, player), 1100);
        sendString(player, GetForlvl(99, player) + "2 Hand Sword" + GetForlvl(14, player), 1088);
        sendString(player, GetForlvl(97, player) + "Kite Shield" + GetForlvl(12, player), 1105);
        sendString(player, GetForlvl(96, player) + "Chain Body" + GetForlvl(11, player), 1098);
        sendString(player, GetForlvl(95, player) + "Battle Axe" + GetForlvl(10, player), 1092);
        sendString(player, GetForlvl(94, player) + "Warhammer" + GetForlvl(9, player), 1083);
        sendString(player, GetForlvl(93, player) + "Square Shield" + GetForlvl(8, player), 1104);
        sendString(player, GetForlvl(92, player) + "Full Helm" + GetForlvl(7, player), 1103);
        sendString(player, GetForlvl(92, player) + "Throwing Knives" + GetForlvl(7, player), 1106);
        sendString(player, GetForlvl(91, player) + "Long Sword" + GetForlvl(6, player), 1086);
        sendString(player, GetForlvl(90, player) + "Scimitar" + GetForlvl(5, player), 1087);
        sendString(player, GetForlvl(90, player) + "Arrowtips" + GetForlvl(5, player), 1108);
        sendString(player, GetForlvl(89, player) + "Sword" + GetForlvl(4, player), 1085);
        sendString(player, GetForlvl(89, player) + "Dart tips" + GetForlvl(4, player), 824);
        sendString(player, GetForlvl(89, player) + "Nails" + GetForlvl(4, player), 13358);
        sendString(player, GetForlvl(88, player) + "Medium Helm" + GetForlvl(3, player), 1102);
        sendString(player, GetForlvl(87, player) + "Mace" + GetForlvl(2, player), 1093);
        sendString(player, GetForlvl(85, player) + "Bolts" + GetForlvl(1, player), 1094);
        sendString(player, GetForlvl(86, player) + "Axe" + GetForlvl(1, player), 1091);
        player.getPacketSender().sendSmithingData(9144, 0, 1119, 10); //dagger
        player.getPacketSender().sendSmithingData(1359, 0, 1120, 1); //axe
        player.getPacketSender().sendSmithingData(1113, 0, 1121, 1); //chain body
        player.getPacketSender().sendSmithingData(1147, 0, 1122, 1); //med helm
        player.getPacketSender().sendSmithingData(824, 0, 1123, 10); //Bolts
        player.getPacketSender().sendSmithingData(1289, 1, 1119, 1); //s-sword
        player.getPacketSender().sendSmithingData(1432, 1, 1120, 1); //mace
        player.getPacketSender().sendSmithingData(1079, 1, 1121, 1); //platelegs
        player.getPacketSender().sendSmithingData(1163, 1, 1122, 1); //full helm
        player.getPacketSender().sendSmithingData(44, 1, 1123, 15); //arrowtips
        player.getPacketSender().sendSmithingData(1333, 2, 1119, 1); //scimmy
        player.getPacketSender().sendSmithingData(1347, 2, 1120, 1); //warhammer
        player.getPacketSender().sendSmithingData(1093, 2, 1121, 1); //plateskirt
        player.getPacketSender().sendSmithingData(1185, 2, 1122, 1); //Sq. Shield
        player.getPacketSender().sendSmithingData(868, 2, 1123, 5); //throwing-knives
        player.getPacketSender().sendSmithingData(1303, 3, 1119, 1); //longsword
        player.getPacketSender().sendSmithingData(1373, 3, 1120, 1); //battleaxe
        player.getPacketSender().sendSmithingData(1127, 3, 1121, 1); //platebody
        player.getPacketSender().sendSmithingData(1201, 3, 1122, 1); //kiteshield
        player.getPacketSender().sendSmithingData(1319, 4, 1119, 1); //2h sword
        player.getPacketSender().sendSmithingData(4824, 4, 1122, 15); //nails
        player.getPacketSender().sendSmithingData(-1, 3, 1123, 1);
        player.getPacketSender().sendSmithingData(3101, 4, 1120, 1); // claws
        ArrayList<Item> itemsColumnOne = new ArrayList<>();
        itemsColumnOne.add(new Item(9144, 1));
        itemsColumnOne.add(new Item(1289, 1));
        itemsColumnOne.add(new Item(1333, 1));
        itemsColumnOne.add(new Item(1303, 1));
        itemsColumnOne.add(new Item(1319, 1));
        player.getPacketSender().sendItemsOnInterface(1119, itemsColumnOne.size(), itemsColumnOne, true);

        /*player.getPacketSender().sendSmithingData(877, 0, 1119, 1);
        player.getPacketSender().sendSmithingData(1277, 0, 1119, 1);
        player.getPacketSender().sendSmithingData(1321, 2, 1119, 1);
        player.getPacketSender().sendSmithingData(1291, 3, 1119, 1);
        player.getPacketSender().sendSmithingData(1307, 4, 1119, 1);*/

        ArrayList<Item> itemsColumnTwo = new ArrayList<>();
        itemsColumnTwo.add(new Item(1359, 1));
        itemsColumnTwo.add(new Item(1432, 1));
        itemsColumnTwo.add(new Item(1347, 1));
        itemsColumnTwo.add(new Item(1373, 1));
        itemsColumnTwo.add(new Item(3101, 1));
        player.getPacketSender().sendItemsOnInterface(1120, itemsColumnTwo.size(), itemsColumnTwo, true);

        /*player.getPacketSender().sendSmithingData(1351, 0, 1120, 1);
        player.getPacketSender().sendSmithingData(1422, 1, 1120, 1);
        player.getPacketSender().sendSmithingData(1337, 2, 1120, 1);
        player.getPacketSender().sendSmithingData(1375, 3, 1120, 1);
        player.getPacketSender().sendSmithingData(3095, 4, 1120, 1);*/

        ArrayList<Item> itemsColumnThree = new ArrayList<>();
        itemsColumnThree.add(new Item(1113, 1));
        itemsColumnThree.add(new Item(1079, 1));
        itemsColumnThree.add(new Item(1093, 1));
        itemsColumnThree.add(new Item(1127, 1));
        player.getPacketSender().sendItemsOnInterface(1121, itemsColumnThree.size(), itemsColumnThree, true);

        /*player.getPacketSender().sendSmithingData(1103, 0, 1121, 1);
        player.getPacketSender().sendSmithingData(1075, 1, 1121, 1);
        player.getPacketSender().sendSmithingData(1087, 2, 1121, 1);
        player.getPacketSender().sendSmithingData(1117, 3, 1121, 1);*/

        ArrayList<Item> itemsColumnFour = new ArrayList<>();
        itemsColumnFour.add(new Item(1147, 1));
        itemsColumnFour.add(new Item(1163, 1));
        itemsColumnFour.add(new Item(1185, 1));
        itemsColumnFour.add(new Item(1201, 1));
        itemsColumnFour.add(new Item(4824, 15));
        player.getPacketSender().sendItemsOnInterface(1122, itemsColumnFour.size(), itemsColumnFour, true);

        /*player.getPacketSender().sendSmithingData(1139, 0, 1122, 1);
        player.getPacketSender().sendSmithingData(1155, 1, 1122, 1);
        player.getPacketSender().sendSmithingData(1173, 2, 1122, 1);
        player.getPacketSender().sendSmithingData(1189, 3, 1122, 1);
        player.getPacketSender().sendSmithingData(4819, 4, 1122, 15);*/

        ArrayList<Item> itemsColumnFive = new ArrayList<>();
        itemsColumnFive.add(new Item(824, 10));
        itemsColumnFive.add(new Item(44, 15));
        itemsColumnFive.add(new Item(868, 5));		
        player.getPacketSender().sendItemsOnInterface(1123, itemsColumnFive.size(), itemsColumnFive, true);				
        sendString(player, "", 1135);
        sendString(player, "", 1134);
        sendString(player, "", 11461);
        sendString(player, "", 11459);
        sendString(player, "", 1132);
        sendString(player, "", 1096);
        player.getPacketSender().sendInterface(994);
    }

    public static void sendString(Player player, String s, int i) {
        player.getPacketSender().sendString(i, s);
    }

    private static String GetForlvl(int i, Player player) {
        if (player.getSkillManager().getMaxLevel(Skill.SMITHING) >= i)
            return "@whi@";

        return "@bla@";
    }

    private static String GetForBars(int i, int j, Player player) {
        if (player.getInventory().getAmount(i) >= j)
            return "@gre@";
        return "@red@";
    }

    public static int getItemAmount(Item item) {
        String name = item.getDefinition().getName().toLowerCase();
        if (name.contains("cannon")) {
            return 4;
        } else if (name.contains("knife")) {
            return 5;
        } else if (name.contains("arrowtips") || name.contains("nails")) {
            return 15;
        } else if (name.contains("dart tip") || name.contains("bolts")) {
            return 10;
        }
        return 1;
    }

    public static int getBarAmount(Item item) {
        String name = item.getDefinition().getName().toLowerCase();
        if (name.contains("scimitar") || name.contains("claws") || name.contains("longsword") || name.contains("sq shield") || name.contains("full helm")) {
            return 2;
        } else if (name.contains("2h sword") || name.contains("warhammer") || name.contains("battleaxe") || name.contains("chainbody") || name.contains("platelegs") || name.contains("plateskirt") || name.contains("kiteshield")) {
            return 3;
        } else if (name.contains("platebody")) {
            return 5;
        }
        return 1;
    }

    public static double getData(Item item, String type) {
        double xp = 1;
        int reqLvl = 1;
        @SuppressWarnings("unused")
        int amount = getItemAmount(item);
        switch (item.getId()) {
            case 2:
                xp = 25.5;
                reqLvl = 35;
                break;
            case 1205: // Bronze dagger
                xp = 12.5;
                reqLvl = 1;
                break;

            case 1351: // Bronze Hatchet
                xp = 12.5;
                reqLvl = 1;
                break;

            case 1422: // Bronze Mace
                xp = 12.5;
                reqLvl = 2;
                break;

            case 1139: // Bronze Med Helm
                xp = 12.5;
                reqLvl = 3;
                break;

            case 9375: // Bronze Bolts
                xp = 12.5;
                reqLvl = 3;
                break;

            case 1277: // Bronze Sword
                xp = 12.5;
                reqLvl = 4;
                break;

            case 4819: // Bronze nails
                xp = 12.5;
                reqLvl = 4;
                break;

            case 1794: // Bronze Wire
                xp = 12.5;
                reqLvl = 4;
                break;

            case 819: // Bronze Dart Tips
                xp = 12.5;
                reqLvl = 4;
                break;

            case 877: // Bronze Bolts
                xp = 12.5;
                reqLvl = 4;
                break;

            case 39: // Bronze Arrowtips
                xp = 12.5;
                reqLvl = 5;
                break;

            case 1321: // Bronze Scimitar
                xp = 25;
                reqLvl = 5;
                break;

            case 1265: // Bronze Pickaxe
                xp = 25;
                reqLvl = 5;
                break;

            case 1291: // Bronze Long Sword
                xp = 25;
                reqLvl = 6;
                break;

            case 9420: // Bronze Limbs
                xp = 12.5;
                reqLvl = 6;
                break;

            case 1155: // Bronze Full Helm
                xp = 25;
                reqLvl = 7;
                break;

            case 864: // Bronze Throwing Knives
                xp = 12.5;
                reqLvl = 7;
                break;

            case 1173: // Bronze Sq Shield
                xp = 25;
                reqLvl = 8;
                break;

            case 1337: // Bronze Warhammer
                xp = 37.5;
                reqLvl = 9;
                break;

            case 1375: // Bronze Battleaxe
                xp = 37.5;
                reqLvl = 10;
                break;

            case 1103: // Bronze Chainbody
                xp = 37.5;
                reqLvl = 11;
                break;

            case 1189: // Bronze Kiteshield
                xp = 37.5;
                reqLvl = 12;
                break;

            case 3095: // Bronze claws
                xp = 25;
                reqLvl = 13;
                break;

            case 1307: // Bronze 2h Sword
                xp = 37.5;
                reqLvl = 14;
                break;

            case 1087: // Bronze Plateskirt
                xp = 37.5;
                reqLvl = 16;
                break;

            case 1075: // Bronze Platelegs
                xp = 37.5;
                reqLvl = 16;
                break;

            case 1117: // Bronze Platebody
                xp = 62.5;
                reqLvl = 18;
                break;

            case 1203: // iron dagger
                xp = 25;
                reqLvl = 15;
                break;

            case 1349: // iron Hatchet
                xp = 25;
                reqLvl = 16;
                break;

            case 1420: // iron mace
                xp = 25;
                reqLvl = 17;
                break;

            case 7225: // iron spit
                xp = 25;
                reqLvl = 17;
                break;

            case 1137: // iron med helm
                xp = 25;
                reqLvl = 18;
                break;

            case 9140: // iron bolt
                xp = 25;
                reqLvl = 18;
                break;

            case 1279: // Iron Sword
                xp = 25;
                reqLvl = 19;
                break;

            case 4820: // iron Nails
                xp = 25;
                reqLvl = 19;
                break;

            case 820: // iron dart Tips
                xp = 25;
                reqLvl = 20;
                break;

            case 40: // iron arrowtips
                xp = 25;
                reqLvl = 20;
                break;

            case 1323: // iron Scimitar
                xp = 50;
                reqLvl = 20;
                break;

            case 1267: // iron Pickaxe
                xp = 50;
                reqLvl = 20;
                break;

            case 1293: // iron Longsword
                xp = 50;
                reqLvl = 21;
                break;

            case 1153: // iron Full Helm
                xp = 50;
                reqLvl = 22;
                break;

            case 863: // iron throwing Knives
                xp = 25;
                reqLvl = 22;
                break;

            case 1175: // iron sq Shield
                xp = 50;
                reqLvl = 23;
                break;

            case 9423: // iron Limbs
                xp = 25;
                reqLvl = 23;
                break;

            case 1335: // iron Warhammer
                xp = 75;
                reqLvl = 24;
                break;

            case 1363: // iron Battleaxe
                xp = 75;
                reqLvl = 25;
                break;

            case 1101: // iron Chainbody
                xp = 75;
                reqLvl = 26;
                break;

            case 4540: // iron Oil Latern Frame
                xp = 25;
                reqLvl = 26;
                break;

            case 1191: // iron Kiteshield
                xp = 75;
                reqLvl = 27;
                break;

            case 3096: // iron Claws
                xp = 50;
                reqLvl = 28;
                break;

            case 1309: // iron 2h Sword
                xp = 75;
                reqLvl = 29;
                break;

            case 1081: // iron Plateskirt
                xp = 75;
                reqLvl = 31;
                break;

            case 1067: // iron Platelegs
                xp = 75;
                reqLvl = 31;
                break;

            case 1115: // iron platebody
                xp = 125;
                reqLvl = 33;
                break;

            case 1207: // steel Dagger
                xp = 37.5;
                reqLvl = 30;
                break;
            case 1353: // steel Hatchet
                xp = 37.5;
                reqLvl = 31;
                break;
            case 1424: // steel Mace
                xp = 37.5;
                reqLvl = 32;
                break;
            case 1141: // steel Med Helm
                xp = 37.5;
                reqLvl = 33;
                break;
            case 9141: // steel bolts
                xp = 37.5;
                reqLvl = 33;
                break;
            case 1539: // steel sword
                xp = 37.5;
                reqLvl = 34;
                break;
            case 1281: // steel sword
                xp = 37.5;
                reqLvl = 34;
                break;
            case 821: // steel dart Tips
                xp = 37.5;
                reqLvl = 34;
                break;
            case 41: // steel Arrowtips
                xp = 37.5;
                reqLvl = 35;
                break;

            case 1325: // steel scimitar
                xp = 75;
                reqLvl = 35;
                break;
            case 1269: // steel Pickaxe
                xp = 75;
                reqLvl = 35;
                break;
            case 1295: // steel Longsword
                xp = 75;
                reqLvl = 36;
                break;
            case 2370: // steel studs
                xp = 37.5;
                reqLvl = 36;
                break;
            case 9425: // steel crossbow limbs
                xp = 37.5;
                reqLvl = 36;
                break;
            case 1157: // steel full helm
                xp = 75;
                reqLvl = 37;
                break;
            case 865: // steel Knifes
                xp = 37.5;
                reqLvl = 37;
                break;
            case 1177: // steel sq Shield
                xp = 75;
                reqLvl = 38;
                break;
            case 1339: // steel Warhammer
                xp = 113;
                reqLvl = 39;
                break;
            case 1365: // steel Battleaxe
                xp = 112.5;
                reqLvl = 40;
                break;
            case 1105: // steel Chainbody
                xp = 112.5;
                reqLvl = 41;
                break;
            case 1193: // steel Kiteshield
                xp = 112.5;
                reqLvl = 42;
                break;
            case 3097: // steel Claws
                xp = 75;
                reqLvl = 43;
                break;
            case 1311: // steel 2h Sword
                xp = 112.5;
                reqLvl = 44;
                break;
            case 1084: // steel Plateskirt
                xp = 112.5;
                reqLvl = 46;
                break;
            case 1069: // steel Platelegs
                xp = 112.5;
                reqLvl = 46;
                break;
            case 1119: // steel Platebody
                xp = 187.5;
                reqLvl = 48;
                break;

            case 1209: // Mithril Dagger
                xp = 50;
                reqLvl = 50;
                break;

            case 1355: // Mithril Hatchet
                xp = 50;
                reqLvl = 51;
                break;

            case 1428: // Mithril Mace
                xp = 50;
                reqLvl = 52;
                break;

            case 1143: // Mithril Med Helm
                xp = 50;
                reqLvl = 53;
                break;

            case 9142: // Mithril bolts
                xp = 50;
                reqLvl = 53;
                break;

            case 1285: // Mithril Sword
                xp = 50;
                reqLvl = 54;
                break;

            case 4822: // Mithril Nails
                xp = 50;
                reqLvl = 54;
                break;

            case 822: // Mithril Dart Tips
                xp = 50;
                reqLvl = 54;
                break;

            case 42: // Mithril Arrowtips
                xp = 50;
                reqLvl = 55;
                break;

            case 1329: // Mithril Scimitar
                xp = 100;
                reqLvl = 55;
                break;

            case 1273: // Mithril Pickaxe
                xp = 100;
                reqLvl = 55;
                break;

            case 1299: // Mithril longsword
                xp = 100;
                reqLvl = 56;
                break;

            case 9427: // Mithril limbs
                xp = 50;
                reqLvl = 56;
                break;

            case 1159: // Mithril Full helm
                xp = 100;
                reqLvl = 57;
                break;

            case 866: // Mithril knifes
                xp = 50;
                reqLvl = 57;
                break;

            case 1181: // Mithril Sq Shield
                xp = 100;
                reqLvl = 58;
                break;

            case 1343: // Mithril warhammer
                xp = 150;
                reqLvl = 59;
                break;

            case 9416: // Mithril grapple Tip
                xp = 100;
                reqLvl = 59;
                break;

            case 1369: // Mithril Battleaxe
                xp = 150;
                reqLvl = 60;
                break;

            case 1109: // Mithril chainbody
                xp = 150;
                reqLvl = 61;
                break;

            case 1197: // Mithril Kiteshield
                xp = 150;
                reqLvl = 62;
                break;

            case 3099: // Mithril claws
                xp = 100;
                reqLvl = 63;
                break;

            case 1315: // Mithril 2h sword
                xp = 150;
                reqLvl = 64;
                break;

            case 1085: // Mithril plateskirt
                xp = 150;
                reqLvl = 66;
                break;

            case 1071: // Mithril plateleggs
                xp = 150;
                reqLvl = 66;
                break;

            case 1121: // Mithril Platebody
                xp = 250;
                reqLvl = 68;
                break;
            case 1211: // Adamant Dagger
                xp = 62.5;
                reqLvl = 70;
                break;
            case 1357: // Adamant Hatchet
                xp = 62.5;
                reqLvl = 71;
                break;
            case 1430: // Adamant Mace
                xp = 62.5;
                reqLvl = 72;
                break;
            case 1145: // Adamant Med Helm
                xp = 62.5;
                reqLvl = 73;
                break;
            case 9143: // Adamant Bolts
                xp = 62.5;
                reqLvl = 73;
                break;
            case 1287: // Adamant Sword
                xp = 62.5;
                reqLvl = 74;
                break;
            case 4823: // Adamant nails
                xp = 62.5;
                reqLvl = 74;
                break;
            case 823: // Adamant dart tips
                xp = 62.5;
                reqLvl = 74;
                break;
            case 43: // Adamant Arrowtips
                xp = 62.5;
                reqLvl = 75;
                break;
            case 1331: // Adamant Scimitar
                xp = 125;
                reqLvl = 75;
                break;
            case 1271: // Adamant Pickaxe
                xp = 125;
                reqLvl = 75;
            case 1301: // Adamant Longsword
                xp = 125;
                reqLvl = 76;
                break;
            case 9429: // Adamant limbs
                xp = 62.5;
                reqLvl = 76;
                break;
            case 1161: // Adamant Full Helm
                xp = 125;
                reqLvl = 77;
                break;
            case 867: // Adamant knifes
                xp = 62.5;
                reqLvl = 77;
                break;
            case 1183: // Adamant Sq Shield
                xp = 125;
                reqLvl = 78;
                break;
            case 1345: // Adamant warhammer
                xp = 187.5;
                reqLvl = 79;
                break;
            case 1371: // Adamant Battleaxe
                xp = 187.5;
                reqLvl = 80;
                break;
            case 1111: // Adamant Chainbody
                xp = 187.5;
                reqLvl = 81;
                break;
            case 1199: // Adamant Kiteshield
                xp = 187.5;
                reqLvl = 82;
                break;
            case 3100: // Adamant Claws
                xp = 125;
                reqLvl = 83;
                break;
            case 1317: // Adamant 2h sword
                xp = 187.5;
                reqLvl = 84;
                break;
            case 1091: // Adamant Plateskirt
                xp = 187.5;
                reqLvl = 86;
                break;
            case 1073: // Adamant Platelegs
                xp = 187.5;
                reqLvl = 86;
                break;
            case 1123: // Adamant Platebody
                xp = 312.5;
                reqLvl = 88;
                break;
            case 1213: // Rune Dagger
                xp = 75;
                reqLvl = 85;
                break;
            case 1359: // Rune Axe
                xp = 75;
                reqLvl = 85;
                break;
            case 1432: // Rune Mace
                xp = 75;
                reqLvl = 86;
                break;
            case 1147: // Rune Med Helm
                xp = 75;
                reqLvl = 87;
                break;
            case 9144: // Rune Bolts
                xp = 75;
                reqLvl = 88;
                break;
            case 1289: // Rune Sword
                xp = 75;
                reqLvl = 89;
                break;
            case 4824: // Rune Nails
                xp = 75;
                reqLvl = 89;
                break;
            case 824: // Rune Dart Tips
                xp = 75;
                reqLvl = 90;
                break;
            case 44: // Rune Arrowtips
                xp = 75;
                reqLvl = 90;
                break;
            case 1333: // Rune Scimitar
                xp = 150;
                reqLvl = 90;
                break;
            case 1275: // Rune Pickaxe
                xp = 150;
                reqLvl = 91;
                break;
            case 1303: // Rune Longsword
                xp = 150;
                reqLvl = 91;
                break;
            case 9431: // Rune Limbs
                xp = 75;
                reqLvl = 91;
                break;
            case 1163: // Rune Full helm
                xp = 150;
                reqLvl = 92;
                break;
            case 868: // Rune Knifes
                xp = 75;
                reqLvl = 92;
                break;
            case 1185: // Rune Sq shield
                xp = 150;
                reqLvl = 93;
                break;
            case 1347: // Rune Warhammer
                xp = 225;
                reqLvl = 94;
                break;
            case 1373: // Rune Battleaxe
                xp = 225;
                reqLvl = 95;
                break;
            case 1113: // Rune Chainbody
                xp = 225;
                reqLvl = 96;
                break;
            case 1201: // Rune Kiteshield
                xp = 225;
                reqLvl = 97;
                break;
            case 3101: // Rune Claws
                xp = 150;
                reqLvl = 98;
                break;
            case 1319: // Rune 2h sword
                xp = 225;
                reqLvl = 99;
                break;
            case 1093: // Rune Plateskirt
                xp = 225;
                reqLvl = 99;
                break;
            case 1079: // Rune Plateleggs
                xp = 225;
                reqLvl = 99;
                break;
            case 1127: // Rune Platebody
                xp = 375;
                reqLvl = 99;
                break;

        }
        return type.equalsIgnoreCase("xp") ? xp : reqLvl;
    }

    public static boolean ironOreSuccess(Player player) {
        boolean ringOfForging = ItemDegrading.handleItemDegrading(player, ItemDegrading.DegradingItem.RING_OF_FORGING);
        return ringOfForging || Misc.getRandom((int) (1 + player.getSkillManager().getCurrentLevel(Skill.SMITHING) / 1.5)) > 5;
    }
}
