package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveOverloadTimer extends SaveInteger {

	public SaveOverloadTimer(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setOverloadPotionTimer(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getOverloadPotionTimer();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
