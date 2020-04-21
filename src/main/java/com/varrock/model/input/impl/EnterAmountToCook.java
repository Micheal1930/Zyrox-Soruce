package com.varrock.model.input.impl;

import com.varrock.model.input.EnterAmount;
import com.varrock.world.content.skill.impl.cooking.Cooking;
import com.varrock.world.entity.impl.player.Player;

public class EnterAmountToCook extends EnterAmount {

	@Override
	public boolean handleAmount(Player player, int amount) {
		if(player.getSelectedSkillingItem() > 0)
			Cooking.cook(player, amount);

		return false;
	}
}
