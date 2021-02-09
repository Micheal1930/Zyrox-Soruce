package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveRfdWave extends SaveInteger {

	public SaveRfdWave(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getMinigameAttributes().getRecipeForDisasterAttributes().setWavesCompleted(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
