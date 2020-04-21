package com.varrock.saving.impl;

import com.varrock.saving.SaveLong;
import com.varrock.world.entity.impl.player.Player;

public class SaveAchievedMax extends SaveLong {

	public SaveAchievedMax(String name) {
		super(name);
	}

	@Override
	public long getDefaultValue() {
		return -1;
	}

	@Override
	public void setValue(Player player, long value) {
		player.achievedMax = value;

	}

	@Override
	public Long getValue(Player player) {
		return player.achievedMax;
	}

}
