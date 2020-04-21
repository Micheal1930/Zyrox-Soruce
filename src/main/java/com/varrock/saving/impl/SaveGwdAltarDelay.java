package com.varrock.saving.impl;

import com.varrock.saving.SaveLong;
import com.varrock.world.entity.impl.player.Player;

public class SaveGwdAltarDelay extends SaveLong {

	public SaveGwdAltarDelay(String name) {
		super(name);
	}

	@Override
	public long getDefaultValue() {
		return 0;
	}

	@Override
	public void setValue(Player player, long value) {
		player.getMinigameAttributes().getGodwarsDungeonAttributes().setAltarDelay(value);
	}

	@Override
	public Long getValue(Player player) {
		return player.getMinigameAttributes().getGodwarsDungeonAttributes().getAltarDelay();
	}

}
