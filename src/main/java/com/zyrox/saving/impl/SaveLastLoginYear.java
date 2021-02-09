package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

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
