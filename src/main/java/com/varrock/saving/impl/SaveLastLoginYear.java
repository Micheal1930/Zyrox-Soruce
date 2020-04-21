package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveLastLoginYear extends SaveInteger {

	public SaveLastLoginYear(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setLastLoginYear(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getLastLoginYear();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
