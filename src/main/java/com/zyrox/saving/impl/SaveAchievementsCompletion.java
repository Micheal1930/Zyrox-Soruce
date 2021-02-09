package com.zyrox.saving.impl;

import com.zyrox.saving.SaveBooleanArray;
import com.zyrox.world.entity.impl.player.Player;

public class SaveAchievementsCompletion extends SaveBooleanArray {

	public SaveAchievementsCompletion(String name) {
		super(name);
	}

	@Override
	public boolean[] getBooleanArray(Player player) {
		return player.getAchievementAttributes().getCompletion();
	}

	@Override
	public void setBooleanValue(Player player, int index, boolean value) {
		if (index > player.getAchievementAttributes().getCompletion().length) {
			return;
		}
		
		player.getAchievementAttributes().setCompletion(index, value); //OK
	}

}
