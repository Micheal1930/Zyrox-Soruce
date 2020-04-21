package com.varrock.saving.impl;

import com.varrock.saving.SaveLong;
import com.varrock.util.Stopwatch;
import com.varrock.world.entity.impl.player.Player;

public class SaveSpecTimer extends SaveLong {

	public SaveSpecTimer(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, long value) {
		if(value > 0) {
			player.specTimer = new Stopwatch(value);
		}
	}

	@Override
	public Long getValue(Player player) {
		return player.specTimer == null ? -1 : player.specTimer.getTime();
	}

	@Override
	public long getDefaultValue() {
		return 0;
	}


}
