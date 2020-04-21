package com.varrock.model.input.impl;

import com.varrock.model.input.EnterAmount;
import com.varrock.world.content.skill.impl.prayer.BonesOnAltar;
import com.varrock.world.entity.impl.player.Player;

public class EnterAmountOfBonesToSacrifice extends EnterAmount {

	@Override
	public boolean handleAmount(Player player, int amount) {
		BonesOnAltar.offerBones(player, amount);
		return false;
	}

}
