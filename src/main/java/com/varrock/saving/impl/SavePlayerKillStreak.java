package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
