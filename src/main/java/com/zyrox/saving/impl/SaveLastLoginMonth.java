package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveLastLoginMonth extends SaveInteger {

	public SaveLastLoginMonth(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setLastLoginMonth(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getLastLoginMonth();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
