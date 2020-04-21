package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveDfsCharges extends SaveInteger {

	public SaveDfsCharges(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.incrementDfsCharges(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getDfsCharges();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
