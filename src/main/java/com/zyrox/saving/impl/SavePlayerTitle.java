package com.zyrox.saving.impl;

import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

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
