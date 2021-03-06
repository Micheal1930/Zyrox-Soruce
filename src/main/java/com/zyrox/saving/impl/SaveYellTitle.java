package com.zyrox.saving.impl;

import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

public class SaveYellTitle extends SaveString {

	public SaveYellTitle(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		player.yellTitle = value;
	}

	@Override
	public String getValue(Player player) {
		return player.yellTitle;
	}

}
