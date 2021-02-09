package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveJailKills extends SaveInteger {

	public SaveJailKills(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setJailKills(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getJailKills();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
