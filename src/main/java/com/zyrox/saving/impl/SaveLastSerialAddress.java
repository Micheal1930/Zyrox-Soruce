package com.zyrox.saving.impl;

import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

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
