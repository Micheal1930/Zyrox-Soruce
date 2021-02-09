package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveSpecialAmount extends SaveInteger {

	public SaveSpecialAmount(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setSpecialPercentage(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getSpecialPercentage();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
