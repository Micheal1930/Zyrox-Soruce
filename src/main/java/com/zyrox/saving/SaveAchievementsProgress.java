package com.zyrox.saving;

import com.zyrox.world.entity.impl.player.Player;

public class SaveAchievementsProgress extends SaveIntArray {

	public SaveAchievementsProgress(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] getIntArray(Player player) {
		// TODO Auto-generated method stub
		return player.getAchievementAttributes().getProgress();
	}

	@Override
	public void setIntValue(Player player, int index, int value) {
		// TODO Auto-generated method stub
		player.getAchievementAttributes().setProgress(index, value);
	}

}
