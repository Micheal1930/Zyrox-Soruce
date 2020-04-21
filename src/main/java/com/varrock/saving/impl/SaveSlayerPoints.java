package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
