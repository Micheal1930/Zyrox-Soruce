package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveBarrowsKillcount extends SaveInteger {

	public SaveBarrowsKillcount(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getMinigameAttributes().getBarrowsMinigameAttributes().setKillcount(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
