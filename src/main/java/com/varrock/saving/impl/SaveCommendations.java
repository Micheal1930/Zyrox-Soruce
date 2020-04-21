package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
