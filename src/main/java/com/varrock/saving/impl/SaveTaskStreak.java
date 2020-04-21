package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveTaskStreak extends SaveInteger {

	public SaveTaskStreak(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getSlayer().setTaskStreak(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getSlayer().getTaskStreak();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
