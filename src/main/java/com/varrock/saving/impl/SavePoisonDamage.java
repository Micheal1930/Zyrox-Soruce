package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
