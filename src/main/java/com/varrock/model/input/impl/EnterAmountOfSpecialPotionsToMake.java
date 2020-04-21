package com.varrock.model.input.impl;

import com.varrock.model.input.EnterAmount;
import com.varrock.world.content.skill.impl.herblore.Herblore;
import com.varrock.world.entity.impl.player.Player;

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
