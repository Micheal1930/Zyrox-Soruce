package com.varrock.saving.impl;

import com.varrock.saving.SaveLong;
import com.varrock.world.entity.impl.player.Player;

public class SaveGameTime extends SaveLong {

	public SaveGameTime(String name) {
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
		player.setTotalPlayTime(value);

	}

	@Override
	public Long getValue(Player player) {
		// TODO Auto-generated method stub
		return player.getTotalPlayTime();
	}

}
