package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveLoyaltyPoints extends SaveInteger {

	public SaveLoyaltyPoints(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getPointsHandler().setLoyaltyPoints(value, false);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPointsHandler().getLoyaltyPoints();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
