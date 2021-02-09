package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveSlayerPoints extends SaveInteger {

	public SaveSlayerPoints(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getPointsHandler().setSlayerPoints(value, false);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPointsHandler().getSlayerPoints();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
