package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveVenomImmunity extends SaveInteger {

	public SaveVenomImmunity(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {

	}

	@Override
	public Integer getValue(Player player) {
		return player.getVenomImmunity();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
