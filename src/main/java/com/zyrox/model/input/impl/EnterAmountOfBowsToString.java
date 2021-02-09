package com.zyrox.model.input.impl;

import com.zyrox.model.input.EnterAmount;
import com.zyrox.world.content.skill.impl.fletching.Fletching;
import com.zyrox.world.entity.impl.player.Player;

public class EnterAmountOfBowsToString extends EnterAmount {

	@Override
	public boolean handleAmount(Player player, int amount) {
		Fletching.stringBow(player, amount);
		return false;
	}

}
