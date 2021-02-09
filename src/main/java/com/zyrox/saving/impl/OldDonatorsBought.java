package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class OldDonatorsBought extends SaveInteger {

	public OldDonatorsBought(String name) {
		super(name);
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

	@Override
	public void setValue(Player player, int value) {
		player.setAmountDonated(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getAmountDonated();
	}

}
