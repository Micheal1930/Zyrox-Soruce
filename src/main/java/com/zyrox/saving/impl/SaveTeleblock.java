package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveTeleblock extends SaveInteger {

	public SaveTeleblock(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setTeleblockTimer(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getTeleblockTimer();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
