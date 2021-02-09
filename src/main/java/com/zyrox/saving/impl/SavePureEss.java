package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

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
