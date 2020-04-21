package com.varrock.saving.impl;

import com.varrock.model.PlayerRights;
import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveRights extends SaveInteger {

	public SaveRights(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setRights(PlayerRights.forOrdinal(value));
	}

	@Override
	public Integer getValue(Player player) {
		return player.getRights().ordinal();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
