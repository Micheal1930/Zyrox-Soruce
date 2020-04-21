package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveRuneEss extends SaveInteger {

	public SaveRuneEss(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setStoredRuneEssence(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getStoredRuneEssence();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
