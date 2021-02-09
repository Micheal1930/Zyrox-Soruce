package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveCommendations extends SaveInteger {

	public SaveCommendations(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.getPointsHandler().setCommendations(value, false);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPointsHandler().getCommendations();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
