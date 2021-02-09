package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveCoinsGambled extends SaveInteger {

	public SaveCoinsGambled(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getAchievementAttributes().setCoinsGambled(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getAchievementAttributes().getCoinsGambled();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
