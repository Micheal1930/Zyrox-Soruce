package com.zyrox.model.input.impl;

import com.zyrox.model.input.EnterAmount;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

public class DonateToWell extends EnterAmount {

	@Override
	public boolean handleAmount(Player player, int amount) {
		World.getWell().add(player, amount);
		return false;
	}

}
