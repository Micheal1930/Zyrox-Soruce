package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.content.ClueScrolls;
import com.zyrox.world.entity.impl.player.Player;

public class SaveCluesCompleted extends SaveInteger {

	public SaveCluesCompleted(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		ClueScrolls.setCluesCompleted(value, false);
	}

	@Override
	public Integer getValue(Player player) {
		return ClueScrolls.getCluesCompleted();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
