package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

public class SaveLastHostAddress extends SaveString {

	public SaveLastHostAddress(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		player.setLastHostAddress(value);
	}

	@Override
	public String getValue(Player player) {
		return player.getHostAddress();
	}

}
