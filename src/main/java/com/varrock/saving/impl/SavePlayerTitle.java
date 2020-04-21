package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

public class SavePlayerTitle extends SaveString {

	public SavePlayerTitle(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		player.setTitle(value);
	}

	@Override
	public String getValue(Player player) {
		return player.getTitle();
	}

}
