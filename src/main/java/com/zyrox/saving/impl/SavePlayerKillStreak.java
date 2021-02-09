package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SavePlayerKillStreak extends SaveInteger {

	public SavePlayerKillStreak(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getPlayerKillingAttributes().setKillstreak(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPlayerKillingAttributes().getKillstreak();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
