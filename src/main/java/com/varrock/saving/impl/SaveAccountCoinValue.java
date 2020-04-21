package com.varrock.saving.impl;

import com.varrock.saving.SaveLong;
import com.varrock.world.entity.impl.player.Player;

public class SaveAccountCoinValue extends SaveLong {

	public SaveAccountCoinValue(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, long value) {

	}

	@Override
	public Long getValue(Player player) {
		// TODO Auto-generated method stub
		return player.getAccountValue().getTotalCoinValue();
	}

	@Override
	public long getDefaultValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
