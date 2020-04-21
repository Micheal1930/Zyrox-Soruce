package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
