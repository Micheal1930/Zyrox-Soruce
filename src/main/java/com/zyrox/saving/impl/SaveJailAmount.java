package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveJailAmount extends SaveInteger {

	public SaveJailAmount(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setJailAmount(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getJailAmount();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
