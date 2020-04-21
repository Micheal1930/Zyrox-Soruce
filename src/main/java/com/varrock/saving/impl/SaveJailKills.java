package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
