package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveBhRank extends SaveInteger {

	public SaveBhRank(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getAppearance().setBountyHunterSkull(value, false);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getAppearance().getBountyHunterSkull();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
