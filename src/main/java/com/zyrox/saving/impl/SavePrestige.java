package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SavePrestige extends SaveInteger {

	public SavePrestige(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.prestige = value;
	}

	@Override
	public Integer getValue(Player player) {
		return player.prestige;
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
