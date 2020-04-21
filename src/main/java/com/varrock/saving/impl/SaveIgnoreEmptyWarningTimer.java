package com.varrock.saving.impl;

import com.varrock.saving.SaveLong;
import com.varrock.util.Stopwatch;
import com.varrock.world.entity.impl.player.Player;

public class SaveIgnoreEmptyWarningTimer extends SaveLong {

	public SaveIgnoreEmptyWarningTimer(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, long value) {
		if(value > 0) {
			player.ignoreEmptyStopwatch = new Stopwatch(value);
		}
	}

	@Override
	public Long getValue(Player player) {
		return player.ignoreEmptyStopwatch == null ? -1 : player.ignoreEmptyStopwatch.getTime();
	}

	@Override
	public long getDefaultValue() {
		return 0;
	}


}
