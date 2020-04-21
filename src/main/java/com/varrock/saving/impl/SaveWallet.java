package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveWallet extends SaveInteger {

	public SaveWallet(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, int value) {
		player.wallet = value;
	}

	@Override
	public Integer getValue(Player player) {
		// TODO Auto-generated method stub
		return player.wallet;
	}

	@Override
	public int getDefaultValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
