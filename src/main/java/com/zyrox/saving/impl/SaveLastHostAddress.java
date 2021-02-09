package com.zyrox.saving.impl;

import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

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
