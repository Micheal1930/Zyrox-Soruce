package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
