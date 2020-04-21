package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
