package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveTargetPercentage extends SaveInteger {

	public SaveTargetPercentage(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getPlayerKillingAttributes().setTargetPercentage(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPlayerKillingAttributes().getTargetPercentage();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
