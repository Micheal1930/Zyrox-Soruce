package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

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
