package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveLoyaltyPointsEarned extends SaveInteger {

	public SaveLoyaltyPointsEarned(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getAchievementAttributes().incrementTotalLoyaltyPointsEarned(value);
	}

	@Override
	public Integer getValue(Player player) {
		return (int) player.getAchievementAttributes().getTotalLoyaltyPointsEarned();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
