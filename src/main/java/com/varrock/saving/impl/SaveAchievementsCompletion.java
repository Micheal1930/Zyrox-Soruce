package com.varrock.saving.impl;

import com.varrock.saving.SaveBooleanArray;
import com.varrock.world.entity.impl.player.Player;

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
