package com.zyrox.saving.impl;

import com.zyrox.model.Gender;
import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

public class SaveGender extends SaveString {

	public SaveGender(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		if(value.length() < 3) {
			return;
		}
		player.getAppearance().setGender(Gender.valueOf(value), false);
	}

	@Override
	public String getValue(Player player) {
		return player.getAppearance().getGender().name();
	}

}
