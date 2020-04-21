package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveHeight extends SaveInteger {

	public SaveHeight(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, int value) {
		player.getPosition().setZ(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPosition().getZ();
	}

	@Override
	public int getDefaultValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
