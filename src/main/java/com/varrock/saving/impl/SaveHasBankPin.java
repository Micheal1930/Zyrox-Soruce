package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

public class SaveHasBankPin extends SaveBoolean {

	public SaveHasBankPin(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.getBankPinAttributes().setHasBankPin(value);
	}

	@Override
	public Boolean getValue(Player player) {
		return player.getBankPinAttributes().hasBankPin();
	}

	@Override
	public boolean getDefaultValue() {
		// TODO Auto-generated method stub
		return false;
	}


}
