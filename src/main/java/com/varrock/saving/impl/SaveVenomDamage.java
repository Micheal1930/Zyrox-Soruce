package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveVenomDamage extends SaveInteger {

	public SaveVenomDamage(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setVenomDamage(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getVenomDamage();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
