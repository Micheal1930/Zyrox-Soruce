package com.zyrox.saving.impl;

import com.zyrox.saving.SaveLong;
import com.zyrox.util.Time;
import com.zyrox.world.entity.impl.player.Player;

public class SaveLastPassReset extends SaveLong {

	public SaveLastPassReset(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, long value) {
		if (System.currentTimeMillis() - value > Time.ONE_DAY) {
			value = 0;
		}
		
		player.setLastPassReset(value);
	}

	@Override
	public Long getValue(Player player) {
		return player.getLastPassReset();
	}
	
	@Override
	public long getDefaultValue() {
		return 0;
	}

}
