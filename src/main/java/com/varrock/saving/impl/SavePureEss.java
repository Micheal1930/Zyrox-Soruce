package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SavePureEss extends SaveInteger {

	public SavePureEss(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setStoredPureEssence(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getStoredPureEssence();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
