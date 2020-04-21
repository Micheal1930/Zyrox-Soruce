package com.varrock.world.content;

import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

/**
 * Scratch cards.
 * 
 * @author Blaketon
 *
 */
public class ScratchCards {
	
	/**
	 * The uncommon chance.
	 */
	private static final double UNCOMMON_CHANCE = (double) 1 / 60;
	
	/**
	 * The rare chance.
	 */
	private static final double RARE_CHANCE = (double) 1 / 500;
	
	/**
	 * The super rare chance.
	 */
	private static final double SUPER_RARE_CHANCE = (double) 1 / 1000; 

	/**
	 * Checks a scratching card.
	 * 
	 * @param player
	 *            the player
	 */
	public static void check(Player player) {
		if (!player.getClickDelay().elapsed(300)) {
			return;
		}
		
		player.getClickDelay().reset();
		
		double random = Misc.randomFloat();
		
		int coins;

		if (random <= SUPER_RARE_CHANCE) {
			coins = Misc.random(40_000_000, 100_000_000);
		} else if (random <= RARE_CHANCE) {
			coins = Misc.random(30_000_000, 40_000_000);
		} else if (random <= UNCOMMON_CHANCE) {
			coins = Misc.random(20_000_000, 30_000_000);
		} else {
			coins = Misc.random(10_000_000, 20_000_000);
		}
		
		player.getInventory().delete(18339, 1);
		player.getInventory().add(995, coins);
		
		player.sendMessage("You received @dre@" + Misc.format(coins) + "</col> Coins as a reward from the Gold bag.");
		
		if (random <= RARE_CHANCE) {
            World.sendMessage("<img=10> <col=996633>" + player.getUsername() + " received @dre@" + Misc.formatNumber(coins) + " </col> from the Gold bag!");
        }
	}

}
