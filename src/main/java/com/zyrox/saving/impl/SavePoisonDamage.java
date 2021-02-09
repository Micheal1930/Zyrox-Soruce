package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SavePoisonDamage extends SaveInteger {

	public SavePoisonDamage(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setPoisonDamage(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPoisonDamage();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
