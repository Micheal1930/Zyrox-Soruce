package com.zyrox.model.input.impl;

import com.zyrox.model.input.EnterAmount;
import com.zyrox.world.content.skill.impl.cooking.Cooking;
import com.zyrox.world.entity.impl.player.Player;

public class EnterAmountToCook extends EnterAmount {

	@Override
	public boolean handleAmount(Player player, int amount) {
		if(player.getSelectedSkillingItem() > 0)
			Cooking.cook(player, amount);

		return false;
	}
}
