package com.zyrox.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.world.content.TreasureChest;

/**
 * Dumps Treasure Island loot
 * @Author Jonny/Aj
 */
public class TreasureIslandLootDumper {

	/**
	 * Dumps LootSystem into a .txt file.
	 * @LootSystem
	 */
	public static void dump() {
		try {
			File file = new File("lists/treasure_island_loot.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.newLine();
			write(bw, "-Always-");
			for (int i2 = 0; i2 < TreasureChest.alwaysRewards.length; i2++) {
				if(TreasureChest.alwaysRewards[i2] == null) {
					continue;
				}
				if(TreasureChest.alwaysRewards[i2].getAmount() > 1) {
					write(bw, "" + TreasureChest.alwaysRewards[i2].getAmount() / 2 + "-" + TreasureChest.alwaysRewards[i2].getAmount() + "x " + ItemDefinition.forId(TreasureChest.alwaysRewards[i2].getId()).getName());
				} else {
					write(bw, "" + TreasureChest.alwaysRewards[i2].getAmount() + "x " + ItemDefinition.forId(TreasureChest.alwaysRewards[i2].getId()).getName());
				}
			}
			
			bw.newLine();
			write(bw, "-Common-");
			for (int i2 = 0; i2 < TreasureChest.commonRewards.length; i2++) {
				if(TreasureChest.commonRewards[i2] == null) {
					continue;
				}
				if(TreasureChest.commonRewards[i2].getAmount() > 1) {
					write(bw, "" + TreasureChest.commonRewards[i2].getAmount() / 2 + "-" + TreasureChest.commonRewards[i2].getAmount() + "x " + ItemDefinition.forId(TreasureChest.commonRewards[i2].getId()).getName());
				} else {
					write(bw, "" + TreasureChest.commonRewards[i2].getAmount() + "x " + ItemDefinition.forId(TreasureChest.commonRewards[i2].getId()).getName());
				}
			}

			bw.newLine();
			write(bw, "-Uncommon (1/5)-");
			for (int i2 = 0; i2 < TreasureChest.uncommonRewards.length; i2++) {
				if(TreasureChest.uncommonRewards[i2] == null) {
					continue;
				}
				int chance = TreasureChest.uncommonRewards.length * 5;
				if(TreasureChest.uncommonRewards[i2].getAmount() > 1) {
					write(bw, "" + TreasureChest.uncommonRewards[i2].getAmount() / 2 + "-" + TreasureChest.uncommonRewards[i2].getAmount() + "x " + ItemDefinition.forId(TreasureChest.uncommonRewards[i2].getId()).getName());
				} else {
					write(bw, "" + TreasureChest.uncommonRewards[i2].getAmount() + "x " + ItemDefinition.forId(TreasureChest.uncommonRewards[i2].getId()).getName());
				}

			}

			bw.newLine();
			write(bw, "-Rare (1/250)-");
			for (int i2 = 0; i2 < TreasureChest.rareRewards.length; i2++) {
				if(TreasureChest.rareRewards[i2] == null) {
					continue;
				}
				int chance = TreasureChest.rareRewards.length * 250;
				if(TreasureChest.rareRewards[i2].getAmount() > 1) {
					write(bw, "" + TreasureChest.rareRewards[i2].getAmount() / 2 + "-" + TreasureChest.rareRewards[i2].getAmount() + "x " + ItemDefinition.forId(TreasureChest.rareRewards[i2].getId()).getName());
				} else {
					write(bw, "" + TreasureChest.rareRewards[i2].getAmount() + "x " + ItemDefinition.forId(TreasureChest.rareRewards[i2].getId()).getName());
				}

			}

			write(bw, "");
			bw.close();

			System.out.println("Successfully finished dumping treasure island loot into lists/treasure_island_loot.txt");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes to a BufferedWriter
	 * @param bw
	 * @param content
	 */
	public static void write(BufferedWriter bw, String content) {
		try {
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
