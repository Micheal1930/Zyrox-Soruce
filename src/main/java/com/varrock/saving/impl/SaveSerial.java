package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

public class SaveSerial extends SaveString {

	public SaveSerial(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		
	}

	@Override
	public String getValue(Player player) {
		return player.getSerialNumber();
	}

}
