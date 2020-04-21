package com.varrock.model.input.impl;

import com.varrock.model.input.EnterAmount;
import com.varrock.world.content.skill.impl.crafting.Flax;
import com.varrock.world.entity.impl.player.Player;

public class EnterAmountToSpin extends EnterAmount {

	@Override
	public boolean handleAmount(Player player, int amount) {
		Flax.spinFlax(player, amount);

		return false;
	}

}
