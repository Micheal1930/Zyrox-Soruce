package com.zyrox.model.input.impl;

import com.zyrox.model.input.EnterAmount;
import com.zyrox.world.content.skill.impl.crafting.Flax;
import com.zyrox.world.entity.impl.player.Player;

public class EnterAmountToSpin extends EnterAmount {

	@Override
	public boolean handleAmount(Player player, int amount) {
		Flax.spinFlax(player, amount);

		return false;
	}

}
