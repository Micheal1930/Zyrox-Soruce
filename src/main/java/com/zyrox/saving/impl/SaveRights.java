package com.zyrox.saving.impl;

import com.zyrox.model.PlayerRights;
import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

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
