package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

public class SaveLastSuperSerial extends SaveString {

	public SaveLastSuperSerial(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		player.setLastSuperSerial(value);
	}

	@Override
	public String getValue(Player player) {
		return player.getLastSuperSerial();
	}

}
