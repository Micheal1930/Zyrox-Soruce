package com.zyrox.world.content;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.util.Misc;
import com.zyrox.util.RandomUtility;
import com.zyrox.world.entity.impl.player.Player;

public class ClueScrolls {

	public static int CluesCompleted;
	public static String currentHint;

	public static final int[] ACTIVE_CLUES = { 2677, 2678, 2679, 2680, 2681, 2682, 2683, 2684, 2685 };
	// digging locations or show reward on reading clue??
	// to-do change name of clue scrolls in item def

	public static final int[] LOW_LEVEL_REWARD = { 1077, 1089, 1107, 1125, 1131, 1129, 1133, 1511, 1168, 1165, 1179,
			1195, 1217, 1283, 1297, 1313, 1327, 1341, 1361, 1367, 1426, 2633, 2635, 2637, 7388, 7386, 7392, 7390, 7396,
			7394, 2631, 7364, 7362, 7368, 7366, 2583, 2585, 2587, 2589, 2591, 2593, 2595, 2597, 7332, 7338, 7344, 7350,
			7356, 2894, 2904, 2914, 2924, 2934 };

	public static final int[] MEDIUM_LEVEL_REWARD = { 2599, 2601, 19323, 19323, 19325, 19325, 19327, 19327, 19329, 19329, 19331, 19331, 2603, 2605, 2607, 2609, 2611, 2613, 7334, 10669, 7346,
			7352, 7358, 7319, 7321, 7323, 7325, 7327, 7372, 7370, 7380, 7378, 2645, 2647, 2649, 2577, 2579, 1073, 1091,
			1099, 1111, 1135, 1124, 1145, 1161, 1169, 1183, 1199, 1211, 1245, 1271, 1287, 1301, 1317, 1332, 1357, 1371,
			1430, 6916, 6918, 6920, 6922, 6924, 10400, 10402, 10416, 10418, 10420, 10422, 10436, 10438, 10446, 10448,
			10450, 10452, 10454, 10456, 6889 };

	public static final int[] HIGH_LEVEL_REWARD = { 1079, 1093, 1113, 1127, 1147, 1163, 1185, 1201, 1275, 1303, 1319,
			1333, 2581, 2577, 2581, 2577, 2581, 19335, 14004, 14005, 14006, 14007, 2577, 6585, 6585, 4151, 14000, 14001, 14002, 14003, 359, 1373, 2491, 2497, 2503, 861, 859, 2581, 2577, 2651, 1079, 1093, 1113, 1127, 1147, 1163, 1185,
			1201, 1275, 1303, 19308, 19311, 19317, 19320, 19314, 1319, 1333, 1359, 1373, 2491, 2497, 2503, 861, 859, 2581, 2577, 2651, 2615, 2617, 2619,
			2621, 2623, 2625, 2627, 2629, 2639, 2641, 2643, 2651, 2653, 2655, 2657, 2659, 2661, 2663, 2665, 2667, 2669,
			2671, 2673, 2675, 7336, 7342, 7348, 7354, 7360, 7374, 7376, 7382, 7384, 7398, 7399, 7400, 3481, 3483, 3485, 3486,
			3488, 1079, 1093, 1113, 1127, 1148, 1164, 1185, 1201, 1213, 1247, 1275, 1289, 1303, 1319, 1333, 1347, 1359,
			1374, 1432, 2615, 2617, 2619, 2621, 2623, 10330, 10338, 10348, 1037, 4565, 14000, 14001, 1050, 14002, 19335,
			14603, 14595, 14003, 10332, 10340, 10346, 10334, 10342, 10350, 10336, 10344, 10352, 10368, 10376, 10384,
			10370, 10378, 10386, 10372, 10380, 6666, 6666, 10388, 10374, 10382, 10390, 10470, 10472, 10474, 10440, 10442, 10444,
			6914, 10458, 10464, 10460, 10468, 10462, 10466 };

	public static final int[] BASIC_STACKS = { 995, 380, 561, 886, 374, 563, 890, 386, 560, 892,15273 };

	private static final String[] HINTS = { "Dig somewhere in the edgeville bank",
			"Dig near the mining guild teleport", "Dig somewhere near the duel arena tele",
			"Dig near one of the slayer masters", "Dig in the area you might see fisherman",
			"Dig near the tele to get chaotics", "Dig near the king of dragons",
			"Dig somewhere near the pest control teleport", "Dig where players plant flowers", "Dig somewhere near varrock square", "Dig somewhere near some chickens", "Dig somewhere on the middle of barrows", "Dig in the cave of the giant bird", "Dig near the first few crawling hands"};

	public static void addClueReward(Player player) {
		if (player.getInventory().getFreeSlots() < 5) {
			player.getPacketSender().sendMessage("You must have atleast 5 free inventory spaces!");
			return;
		}
		
		if (!player.getInventory().contains(2714)) {//this should be fine i need to add this casket id
			return;
		}

		player.getInventory().delete(2714, 1);

		player.getInventory().add(LOW_LEVEL_REWARD[Misc.getRandom(LOW_LEVEL_REWARD.length - 1)], 1, "Clue reward");
		player.getInventory().add(LOW_LEVEL_REWARD[Misc.getRandom(LOW_LEVEL_REWARD.length - 1)], 1, "Clue reward");
		player.getInventory().add(BASIC_STACKS[Misc.getRandom(BASIC_STACKS.length - 1)], RandomUtility.RANDOM.nextInt(200) + Misc.random(60), "Clue reward");

		if (RandomUtility.RANDOM.nextInt(3) == 2) {
			player.getInventory().add(MEDIUM_LEVEL_REWARD[Misc.getRandom(MEDIUM_LEVEL_REWARD.length - 1)], 1, "Clue reward");
		}
		if (RandomUtility.RANDOM.nextInt(10) == 5) {
			player.getInventory().add(HIGH_LEVEL_REWARD[Misc.getRandom(HIGH_LEVEL_REWARD.length - 1)], 1, "Clue reward");
		}

	}

	public static void giveHint(Player player, int itemId) {
		if (itemId == 2677) {
			int index = 0;
			currentHint = HINTS[index];
			player.getPacketSender().sendInterface(47700);
			player.getPacketSender().sendString(47704, currentHint);
			player.getPacketSender().sendString(47703, " " + CluesCompleted);

		}
		if (itemId == 2678) {
			int index = 1;
			currentHint = HINTS[index];
			player.getPacketSender().sendInterface(47700);
			player.getPacketSender().sendString(47704, currentHint);
			player.getPacketSender().sendString(47703, " " + CluesCompleted);

		}
		if (itemId == 2679) {
			int index = 2;
			currentHint = HINTS[index];
			player.getPacketSender().sendInterface(47700);
			player.getPacketSender().sendString(47704, currentHint);
			player.getPacketSender().sendString(47703, " " + CluesCompleted);

		}
		if (itemId == 2680) {
			int index = 3;
			currentHint = HINTS[index];
			player.getPacketSender().sendInterface(47700);
			player.getPacketSender().sendString(47704, currentHint);
			player.getPacketSender().sendString(47703, " " + CluesCompleted);

		}
		if (itemId == 2681) {
			int index = 4;
			currentHint = HINTS[index];
			player.getPacketSender().sendInterface(47700);
			player.getPacketSender().sendString(47704, currentHint);
			player.getPacketSender().sendString(47703, " " + CluesCompleted);

		}
		if (itemId == 2682) {
			int index = 5;
			currentHint = HINTS[index];
			player.getPacketSender().sendInterface(47700);
			player.getPacketSender().sendString(47704, currentHint);
			player.getPacketSender().sendString(47703, " " + CluesCompleted);

		}
		if (itemId == 2683) {
			int index = 6;
			currentHint = HINTS[index];
			player.getPacketSender().sendInterface(47700);
			player.getPacketSender().sendString(47704, currentHint);
			player.getPacketSender().sendString(47703, " " + CluesCompleted);

		}
		if (itemId == 2684) {
			int index = 7;
			currentHint = HINTS[index];
			player.getPacketSender().sendInterface(47700);
			player.getPacketSender().sendString(47704, currentHint);
			player.getPacketSender().sendString(47703, " " + CluesCompleted);

		}
		if (itemId == 2685) {
			int index = 8;
			currentHint = HINTS[index];
			player.getPacketSender().sendInterface(47700);
			player.getPacketSender().sendString(47704, currentHint);
			player.getPacketSender().sendString(47703, " " + CluesCompleted);

		}
		if (itemId == 2686) {
			int index = 9;
			currentHint = HINTS[index];
			player.getPacketSender().sendInterface(47700);
			player.getPacketSender().sendString(47704, currentHint);
			player.getPacketSender().sendString(47703, " " + CluesCompleted);

		}
		if (itemId == 2687) {
			int index = 10;
			currentHint = HINTS[index];
			player.getPacketSender().sendInterface(47700);
			player.getPacketSender().sendString(47704, currentHint);
			player.getPacketSender().sendString(47703, " " + CluesCompleted);

		}
		if (itemId == 2688) {
			int index = 11;
			currentHint = HINTS[index];
			player.getPacketSender().sendInterface(47700);
			player.getPacketSender().sendString(47704, currentHint);
			player.getPacketSender().sendString(47703, " " + CluesCompleted);

		}
		if (itemId == 2689) {
			int index = 12;
			currentHint = HINTS[index];
			player.getPacketSender().sendInterface(47700);
			player.getPacketSender().sendString(47704, currentHint);
			player.getPacketSender().sendString(47703, " " + CluesCompleted);

		}
		if (itemId == 2690) {
			int index = 13;
			currentHint = HINTS[index];
			player.getPacketSender().sendInterface(47700);
			player.getPacketSender().sendString(47704, currentHint);
			player.getPacketSender().sendString(47703, " " + CluesCompleted);

		}

	}

	public static void setCluesCompleted(int CluesCompleted, boolean add) {
		if (add)
			CluesCompleted += CluesCompleted;
		else
			ClueScrolls.CluesCompleted = CluesCompleted;
	}

	public static void incrementCluesCompleted(double amount) {
		CluesCompleted += amount;
	}

	public static int getCluesCompleted() {
		return CluesCompleted;
	}

	public static void dumpRewards(File file) throws IOException {
		final BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		writer.write("Common Rewards");
		writer.newLine();
		for (Integer item : LOW_LEVEL_REWARD) {
			ItemDefinition itemDefinition = ItemDefinition.forId(item);

			writer.write("\t" + itemDefinition.getName());
			writer.newLine();
		}

		writer.write("Uncommon Rewards");
		writer.newLine();
		for (Integer item : MEDIUM_LEVEL_REWARD) {
			ItemDefinition itemDefinition = ItemDefinition.forId(item);

			writer.write("\t" + itemDefinition.getName());
			writer.newLine();
		}

		writer.write("Rare Rewards");
		writer.newLine();
		for (Integer item : HIGH_LEVEL_REWARD) {
			ItemDefinition itemDefinition = ItemDefinition.forId(item);

			writer.write("\t" + itemDefinition.getName());
			writer.newLine();
		}

		writer.close();
	}

}
