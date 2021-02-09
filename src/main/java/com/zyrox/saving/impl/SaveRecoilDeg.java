package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

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
