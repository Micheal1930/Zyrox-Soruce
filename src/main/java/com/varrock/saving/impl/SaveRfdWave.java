package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
