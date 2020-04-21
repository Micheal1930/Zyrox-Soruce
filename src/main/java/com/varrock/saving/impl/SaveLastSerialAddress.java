package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

public class SaveLastSerialAddress extends SaveString {

	public SaveLastSerialAddress(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		player.setLastSerialNumber(value);
	}

	@Override
	public String getValue(Player player) {
		return player.getLastSerialNumber();
	}

}
