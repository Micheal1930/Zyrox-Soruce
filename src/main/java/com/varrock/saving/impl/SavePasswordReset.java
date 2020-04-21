package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
