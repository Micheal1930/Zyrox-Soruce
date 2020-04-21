package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
