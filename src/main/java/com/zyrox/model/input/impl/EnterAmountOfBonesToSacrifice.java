package com.zyrox.model.input.impl;

import com.zyrox.model.input.EnterAmount;
import com.zyrox.world.content.skill.impl.prayer.BonesOnAltar;
import com.zyrox.world.entity.impl.player.Player;

public class EnterAmountOfBonesToSacrifice extends EnterAmount {

	@Override
	public boolean handleAmount(Player player, int amount) {
		BonesOnAltar.offerBones(player, amount);
		return false;
	}

}
