package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveEffigy extends SaveInteger {

	public SaveEffigy(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setEffigy(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getEffigy();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
