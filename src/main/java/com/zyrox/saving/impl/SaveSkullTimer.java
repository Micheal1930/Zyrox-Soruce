package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveSkullTimer extends SaveInteger {

	public SaveSkullTimer(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setSkullTimer(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getSkullTimer();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
