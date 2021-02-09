package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveRandomCoffin extends SaveInteger {

	public SaveRandomCoffin(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getMinigameAttributes().getBarrowsMinigameAttributes().setRandomCoffin(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
