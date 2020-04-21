package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveTaskAmount extends SaveInteger {

	public SaveTaskAmount(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getSlayer().setAmountToSlay(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getSlayer().getAmountToSlay();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
