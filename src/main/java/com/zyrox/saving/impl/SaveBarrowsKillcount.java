package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

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
