package com.zyrox.model.input.impl;

import com.zyrox.model.input.EnterAmount;
import com.zyrox.world.content.skill.impl.herblore.Herblore;
import com.zyrox.world.entity.impl.player.Player;

public class EnterAmountOfSpecialPotionsToMake extends EnterAmount {

	private Herblore.SpecialPotion specialPotion;

	public EnterAmountOfSpecialPotionsToMake(Herblore.SpecialPotion specialPotion) {
		this.specialPotion = specialPotion;
	}

	@Override
	public boolean handleAmount(Player player, int amount) {
		Herblore.finishMakingSpecialPotion(player, amount, specialPotion);
		return false;
	}

}
