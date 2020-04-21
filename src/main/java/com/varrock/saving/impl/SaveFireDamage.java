package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveFireDamage extends SaveInteger {

	public SaveFireDamage(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setDragonFireImmunity(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getFireImmunity();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
