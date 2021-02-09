package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SavePassResetCount extends SaveInteger {

	public SavePassResetCount(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setPassResetCount(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPassResetCount();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
