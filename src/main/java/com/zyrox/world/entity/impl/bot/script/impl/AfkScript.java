package com.zyrox.world.entity.impl.bot.script.impl;

import com.zyrox.model.Item;
import com.zyrox.util.Misc;
import com.zyrox.world.entity.impl.bot.script.BotScript;

public class AfkScript extends BotScript {
	
	private int[][] EQUIPMENT = new int[][] {
		{ 4089, 2412, 1712, 4675, 4091, 1540, -1, 4093, -1, 4095, 4097 }, // Mage gear
		{ 10828, 6570, 1704, 9185, 2503, 1540, -1, 2497, -1, 2491, 2577 }, // Black d'hide & Rune crossbow
		{ 3749, 10499, 1712, 861, 2503, -1, -1, 2497, -1, 2491, 6328 }, // Black d'hide & Magic shortbow
		{ 10828, 6570, 1725, 4587, 1127, 1201, -1, 1079 }, //Neitz, full rune & dscim
		{ 4716, 6570, 6585, 4718, 4720, -1, -1, 4722, -1, 7462, 11840 }, // Dharok's
		{ 4753, 6570, 6585, 4755, 4757, -1, -1, 4759, -1, 7462, 11840 }, // Verac's
		{ 6109, 6111, -1, -1, 6107, -1, -1, 6108, -1, 6110, 6106 }, // Ghostly
		{ -1, -1, -1, 4151 } // Whip
	};

	@Override
	public void initialize() {
		player.randomizeLevels();
		
		if (Misc.random(5) == 1) {
			return;
		}

		int[] randomEquipment = Misc.randomElement(EQUIPMENT);
		
		for (int i = 0; i < randomEquipment.length; i++) {
			player.getEquipment().set(i, new Item(randomEquipment[i]));
		}

	}

	@Override
	public void execute() {

	}

	@Override
	public void onStop() {

	}

}
