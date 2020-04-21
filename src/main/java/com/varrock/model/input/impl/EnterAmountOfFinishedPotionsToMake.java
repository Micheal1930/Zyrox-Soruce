package com.varrock.model.input.impl;

import com.varrock.model.input.EnterAmount;
import com.varrock.world.content.skill.impl.herblore.Herblore;
import com.varrock.world.entity.impl.player.Player;

public class EnterAmountOfFinishedPotionsToMake extends EnterAmount {

	@Override
	public boolean handleAmount(Player player, int amount) {
		Herblore.makeFinishedPotions(player, amount);
		return false;
	}

}
