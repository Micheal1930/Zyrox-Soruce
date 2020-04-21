package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveTriviaPoints extends SaveInteger {

	public SaveTriviaPoints(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getPointsHandler().setTriviaPoints(value, false);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPointsHandler().getTriviaPoints();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
