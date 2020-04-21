package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveRecoilDeg extends SaveInteger {

	public SaveRecoilDeg(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setRecoilCharges(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getRecoilCharges();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
