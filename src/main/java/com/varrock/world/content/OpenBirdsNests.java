package com.varrock.world.content;

import com.varrock.util.Misc;
import com.varrock.world.entity.impl.player.Player;

public class OpenBirdsNests {
	
	public static final int loots[] = {1635, 1637, 1639, 1641, 1643};
	public static final int[] seeds = {5312, 5313, 5314, 5315, 5316, 5283, 5284, 5285, 5286, 5287, 5288, 5289, 5290, 5317};

	
	public static void openNest(Player player, int itemId) {
		if(player.getInventory().getFreeSlots() < 2) {
			player.getPA().sendMessage("You need atleast 2 empty slots!");
			return;
		}
		if(itemId == 5073) {
			int s = randomSeeds();
			player.getInventory().add(s, 1, "Birdsnest");
			player.getInventory().delete(itemId, 1);
		} else {
		int r = randomLoots();
		player.getInventory().add(r, 1, "Birdsnest");
		player.getInventory().delete(itemId, 1);
		
		if(Misc.getRandom(250) == 50) {
			player.getInventory().add(2572, 1, "Birdsnest");
		}
		if(Misc.getRandom(300) == 50) {
			player.getInventory().add(995, 75000, "Birds nest");
		}
		if(Misc.getRandom(225) == 50) {
			player.getInventory().add(18782, 1, "Birdsnest");
		}
		}
		player.getInventory().add(6693, 1);
	}
	
	public static int randomLoots() {
		return loots[(int) (Math.random() * loots.length)];
	}
	public static int randomSeeds() {
		return seeds[(int) (Math.random() * seeds.length)];
	}


}
