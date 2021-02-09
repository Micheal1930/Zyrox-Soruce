package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SavePlayerDeaths extends SaveInteger {

	public SavePlayerDeaths(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getPlayerKillingAttributes().setPlayerDeaths(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPlayerKillingAttributes().getPlayerDeaths();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
