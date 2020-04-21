package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
