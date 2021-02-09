package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SavePasswordReset extends SaveInteger {

	public SavePasswordReset(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setPasswordPlayer(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPasswordPlayer();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
