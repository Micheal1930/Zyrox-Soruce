package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.content.ClueScrolls;
import com.varrock.world.entity.impl.player.Player;

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
