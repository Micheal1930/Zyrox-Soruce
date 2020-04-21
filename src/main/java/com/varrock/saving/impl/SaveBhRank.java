package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
