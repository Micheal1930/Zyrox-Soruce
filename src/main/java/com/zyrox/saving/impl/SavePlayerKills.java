package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SavePlayerKills extends SaveInteger {

	public SavePlayerKills(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getPlayerKillingAttributes().setPlayerKills(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPlayerKillingAttributes().getPlayerKills();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
