package com.varrock.model.input.impl;

import com.varrock.model.input.EnterAmount;
import com.varrock.world.content.skill.impl.fletching.Fletching;
import com.varrock.world.entity.impl.player.Player;

public class EnterAmountOfBowsToString extends EnterAmount {

	@Override
	public boolean handleAmount(Player player, int amount) {
		Fletching.stringBow(player, amount);
		return false;
	}

}
