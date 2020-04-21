package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
