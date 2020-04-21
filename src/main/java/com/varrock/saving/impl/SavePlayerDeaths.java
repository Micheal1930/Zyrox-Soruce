package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
