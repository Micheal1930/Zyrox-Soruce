package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
