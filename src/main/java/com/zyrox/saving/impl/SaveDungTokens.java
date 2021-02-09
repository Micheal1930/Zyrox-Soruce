package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveDungTokens extends SaveInteger {

	public SaveDungTokens(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getPointsHandler().setDungeoneeringTokens(value, false);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPointsHandler().getDungeoneeringTokens();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
