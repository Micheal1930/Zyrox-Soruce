package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
