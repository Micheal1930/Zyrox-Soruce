package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

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
