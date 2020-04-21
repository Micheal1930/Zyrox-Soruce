package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

public class SaveName extends SaveString {

	public SaveName(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, String value) {
		player.setUsername(value);
	}

	@Override
	public String getValue(Player player) {
		return player.getName();
	}

}
