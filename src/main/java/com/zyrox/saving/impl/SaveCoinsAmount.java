package com.zyrox.saving.impl;

import com.zyrox.saving.SaveLong;
import com.zyrox.world.entity.impl.player.Player;

public class SaveCoinsAmount extends SaveLong {

	public SaveCoinsAmount(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public long getDefaultValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setValue(Player player, long value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Long getValue(Player player) {
		// TODO Auto-generated method stub
		return player.getAccountValue().getCoinsAmount();
	}

}
