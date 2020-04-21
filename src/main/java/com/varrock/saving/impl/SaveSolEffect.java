package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveSolEffect extends SaveInteger {

	public SaveSolEffect(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setStaffOfLightEffect(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getStaffOfLightEffect();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
