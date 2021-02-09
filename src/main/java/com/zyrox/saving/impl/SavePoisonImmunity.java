package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SavePoisonImmunity extends SaveInteger {

	public SavePoisonImmunity(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setPoisonImmunity(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPoisonImmunity();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
