package com.zyrox.world.content;

import com.zyrox.model.GroundItem;
import com.zyrox.model.Item;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.GroundItemManager;
import com.zyrox.world.entity.impl.player.Player;

public class Artifacts {

	public static int artifacts[] = {14876, 14877, 14878, 14879, 14880, 14881, 14882, 14883, 14884, 14885, 14886, 14887, 14888, 14889, 14890, 14891, 14892};

	public static void sellArtifacts(Player c) {
		c.getPacketSender().sendInterfaceRemoval();
		boolean artifact = false;
		for(int k = 0; k < artifacts.length; k++) {
			if(c.getInventory().contains(artifacts[k])) {
				artifact = true;
			}
		}
		if(!artifact) {
			c.getPacketSender().sendMessage("You do not have any Artifacts in your inventory to sell to BloodMoneyDealer.");
			return;
		}
		for(int i = 0; i < artifacts.length; i++) {
			for(Item item : c.getInventory().getValidItems()) {
				if(item.getId() == artifacts[i]) {
					c.getInventory().delete(artifacts[i], 1);
					c.getInventory().add(995, ItemDefinition.forId(artifacts[i]).getValue(), "Pvp artifact " + artifacts[i]);
					c.getInventory().refreshItems();
				}
			}
		}
		c.getPacketSender().sendMessage("You've sold your artifacts.");

	}
	
	/*
	 * Artifacts
	 */
	private final static int[] LOW_ARTIFACTS = { 14888, 14889, 14890, 14891, 14892 };
	private final static int[] MED_ARTIFACTS = { 14883, 14884, 14885, 14886 };
	private final static int[] HIGH_ARTIFACTS = { 14878, 14879, 14880, 14881, 14882 };
	private final static int[] EXR_ARTIFACTS = { 14876, 14877 };
	private final static int[] PVP_ARMORS = { 13899, 13893, 13887, 13902, 13896, 13890, 13858, 13861};
	
	private final static int[] RARE_LOOTS = { 13899, 13893, 13887, 13902, 13896, 13890, 13858, 13861, 14484, 11694, 13867, 13899, 13902, 20000, 20001, 20002, 4706, 12601, 15486, 11283, 11696, 11698, 11700, 4151, 15241 };
	/**
	 * Handles a target drop
	 * @param Player player		Player who has killed Player o
	 * @param Player o			Player who has been killed by Player player
	 */
	public static void handleDrops(Player killer, Player death, boolean targetKill) {
		if(Misc.getRandom(100) >= 85 || targetKill)
			GroundItemManager.spawnGroundItem(killer, new GroundItem(new Item(getRandomItem(LOW_ARTIFACTS)), death.getPosition().copy(), killer.getUsername(), false, 110, true, 100));
		if (Misc.getRandom(100) >= 90)
			GroundItemManager.spawnGroundItem(killer, new GroundItem(new Item(getRandomItem(MED_ARTIFACTS)), death.getPosition().copy(), killer.getUsername(), false, 110, true, 100));
		if(Misc.getRandom(100) >= 97)
			GroundItemManager.spawnGroundItem(killer, new GroundItem(new Item(getRandomItem(HIGH_ARTIFACTS)), death.getPosition().copy(), killer.getUsername(), false, 110, true, 100));
		if(Misc.getRandom(100) >= 99)
			GroundItemManager.spawnGroundItem(killer, new GroundItem(new Item(getRandomItem(PVP_ARMORS)), death.getPosition().copy(), killer.getUsername(), false, 110, true, 100));
		if(Misc.getRandom(100) >= 99) {
			int rareDrop = getRandomItem(EXR_ARTIFACTS);
			String itemName = Misc.formatText(ItemDefinition.forId(rareDrop).getName());
			GroundItemManager.spawnGroundItem(killer, new GroundItem(new Item(rareDrop), death.getPosition().copy(), killer.getUsername(), false, 110, true, 100));
			World.sendMessage("<img=467>"+killer.getUsername()+" has just received "+Misc.anOrA(itemName)+" "+itemName+" from an artifact!");
		}
		if(Misc.getRandom(100) >= 92) {
			int rareDrop = getRandomItem(RARE_LOOTS);
			String itemName = Misc.formatText(ItemDefinition.forId(rareDrop).getName());
			GroundItemManager.spawnGroundItem(killer, new GroundItem(new Item(rareDrop), death.getPosition().copy(), killer.getUsername(), false, 110, true, 100));
			World.sendMessage("<img=467>"+killer.getUsername()+" has just received "+Misc.anOrA(itemName)+" "+itemName+" from an artifact!");
		}
	}
	
	/**
	 * Get's a random int from the array specified
	 * @param array	The array specified
	 * @return		The random integer
	 */
	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

}
