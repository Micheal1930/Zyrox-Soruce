package com.varrock.world.content.skill.impl.herblore;

import com.varrock.world.content.skill.impl.herblore.decanting.DecantableFlask;
import com.varrock.world.entity.impl.player.Player;

/**
 * 
 * @author Jason MacKeigan (http://www.rune-server.org/members/jason) - base.
 * @author Levi Patton (http://www.rune-server.org/members/auguryps) - Converted.
 * @since  September 16, 2016
 *
 */

public class Decanting {
	
	
	public static void checkRequirements(Player player){
		if (player.getAmountDonated() > 19) {
			startDecanting(player);
			startDecanting(player);
			startDecanting(player);
			startDecanting(player);
			DecantableFlask.decantAllFlask(player);
			player.getPacketSender().sendMessage("@blu@Success! Your potions have been combined.");
			return;
		}

		if(player.getInventory().getAmount(995) >= 250000) {
			player.getInventory().delete(995, 250000);
		} else if(player.getMoneyInPouch() >= 250_000) {
			player.setMoneyInPouch(player.getMoneyInPouch() - 250_000);
			player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch() + "");
		} else {
			player.getPacketSender().sendMessage("You need @red@250,000 Coins</col> to combine your potions");
			return;
		}

		startDecanting(player);
		startDecanting(player);
		startDecanting(player);
		startDecanting(player);
		DecantableFlask.decantAllFlask(player);
		player.getPacketSender().sendMessage("@blu@Success! Your potions have been combined for a fee of @red@250,000 coins.");
	}
	
	public static void startDecanting(Player player) {
		for(Potion p : Potion.values()) {

			int full = p.getFullId();
			int half = p.getHalfId();
			int quarter = p.getQuarterId();
			int threeQuarters = p.getThreeQuartersId();

			int totalDoses = 0;
			int totalEmptyPots = 0;

			if(player.getInventory().contains(threeQuarters)) {
				int amount = player.getInventory().getAmount(threeQuarters);
				totalDoses += (3 * amount);
				totalEmptyPots += amount;
				player.getInventory().delete(threeQuarters, amount);
			}

			if(player.getInventory().contains(half)) {
				int amount = player.getInventory().getAmount(half);
				totalDoses += (2 * amount);
				totalEmptyPots += amount;
				player.getInventory().delete(half, amount);
			}

			if(player.getInventory().contains(quarter)) {
				int amount = player.getInventory().getAmount(quarter);
				totalDoses += amount;
				totalEmptyPots += amount;
				player.getInventory().delete(quarter, amount);
			}

			while (totalDoses > 0) {
				if (totalDoses >= 4) {
					player.getInventory().add(full,  1, "Decanting");
					totalDoses -= 4;
					totalEmptyPots--;
				} else if (totalDoses == 3) {
					player.getInventory().add(threeQuarters, 1, "Decanting");
					totalDoses -= 3;
					totalEmptyPots--;
				} else if (totalDoses == 2) {
					player.getInventory().add(half, 1, "Decanting");
					totalDoses -= 2;
					totalEmptyPots--;
				} else {
					player.getInventory().add(quarter, 1, "Decanting");
					totalDoses -= 1;
					totalEmptyPots--;
				}
			}

			player.getInventory().add(229, totalEmptyPots, "Decanting");
		}
		
	}


}