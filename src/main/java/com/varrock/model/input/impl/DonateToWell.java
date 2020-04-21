package com.varrock.model.input.impl;

import com.varrock.model.input.EnterAmount;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

public class DonateToWell extends EnterAmount {

	@Override
	public boolean handleAmount(Player player, int amount) {
		World.getWell().add(player, amount);
		return false;
	}

}
