package com.zyrox.saving.impl;

import com.zyrox.saving.SaveLong;
import com.zyrox.world.entity.impl.player.Player;

public class SaveTicketsAmount extends SaveLong {

	public SaveTicketsAmount(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, long value) {

	}

	@Override
	public Long getValue(Player player) {
		// TODO Auto-generated method stub
		return player.getAccountValue().getTicketsAmount();
	}

	@Override
	public long getDefaultValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
