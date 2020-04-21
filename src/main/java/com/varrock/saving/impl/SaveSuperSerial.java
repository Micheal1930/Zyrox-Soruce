package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

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
