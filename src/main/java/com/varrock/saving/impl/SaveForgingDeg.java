package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveForgingDeg extends SaveInteger {

	public SaveForgingDeg(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setForgingCharges(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getForgingCharges();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
