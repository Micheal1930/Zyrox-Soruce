package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveAccountValue extends SaveInteger {

	public SaveAccountValue(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, int value) {

	}

	@Override
	public Integer getValue(Player player) {
		// TODO Auto-generated method stub
		return player.getAccountValue().getTotalValue();
	}

	@Override
	public int getDefaultValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
