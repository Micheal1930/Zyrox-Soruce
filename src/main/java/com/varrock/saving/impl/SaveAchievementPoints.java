package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveAchievementPoints extends SaveInteger {

	public SaveAchievementPoints(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, int value) {
		player.getPointsHandler().setAchievementPoints(value, false);
	}

	@Override
	public Integer getValue(Player player) {
		// TODO Auto-generated method stub
		return player.getPointsHandler().getAchievementPoints();
	}

	@Override
	public int getDefaultValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
