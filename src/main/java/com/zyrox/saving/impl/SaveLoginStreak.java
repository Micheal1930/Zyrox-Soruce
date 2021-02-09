package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveLoginStreak extends SaveInteger {

	public SaveLoginStreak(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setLoginStreak(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getLoginStreak();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
