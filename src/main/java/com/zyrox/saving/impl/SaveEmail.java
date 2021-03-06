package com.zyrox.saving.impl;

import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

public class SaveEmail extends SaveString {

	public SaveEmail(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, String value) {
		player.setEmailAddress(value);
	}

	@Override
	public String getValue(Player player) {
		return player.getEmailAddress() == null ? "null" : player.getEmailAddress().trim();
	}

}
