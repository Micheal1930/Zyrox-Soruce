package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveSummonDeath extends SaveInteger {

	public SaveSummonDeath(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		if (value > 0 && player.getSummoning().getSpawnTask() != null)
			player.getSummoning().getSpawnTask().setDeathTimer(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getSummoning().getFamiliar() != null ? player.getSummoning().getFamiliar().getDeathTimer() : -1;
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
