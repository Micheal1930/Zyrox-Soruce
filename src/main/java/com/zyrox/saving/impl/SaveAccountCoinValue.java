package com.zyrox.saving.impl;

import com.zyrox.saving.SaveLong;
import com.zyrox.world.entity.impl.player.Player;

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
