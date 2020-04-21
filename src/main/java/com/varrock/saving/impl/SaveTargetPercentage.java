package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
