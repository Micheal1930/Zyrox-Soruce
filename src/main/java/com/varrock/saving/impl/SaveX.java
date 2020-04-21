package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveX extends SaveInteger {

	public SaveX(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, int value) {
		player.getPosition().setX(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPosition().getX();
	}

	@Override
	public int getDefaultValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
