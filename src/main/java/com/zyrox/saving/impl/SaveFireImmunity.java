package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveFireImmunity extends SaveInteger {

	public SaveFireImmunity(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {

	}

	@Override
	public Integer getValue(Player player) {
		return player.getFireDamageModifier();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
