package com.varrock.saving.impl;

import com.varrock.model.Gender;
import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

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
