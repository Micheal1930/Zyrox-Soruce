package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveEffigy extends SaveInteger {

	public SaveEffigy(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setEffigy(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getEffigy();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
