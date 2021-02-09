package com.zyrox.saving.impl;

import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

public class SaveSuperSerial extends SaveString {

	public SaveSuperSerial(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		
	}

	@Override
	public String getValue(Player player) {
		return player.getSuperSerialNumber();
	}

}
