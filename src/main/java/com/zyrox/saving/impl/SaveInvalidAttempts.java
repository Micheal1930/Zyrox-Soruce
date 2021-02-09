package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveInvalidAttempts extends SaveInteger {

	public SaveInvalidAttempts(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, int value) {
		player.getBankPinAttributes().setInvalidAttempts(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getBankPinAttributes().getInvalidAttempts();
	}

	@Override
	public int getDefaultValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
