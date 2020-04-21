package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveLastLoginDate extends SaveInteger {

	public SaveLastLoginDate(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setLastLoginDate(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getLastLoginDate();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
