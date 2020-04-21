package com.varrock.saving.impl;

import com.varrock.model.Prayerbook;
import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

public class SavePrayerBook extends SaveString {

	public SavePrayerBook(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		player.setPrayerbook(Prayerbook.valueOf(value));
	}

	@Override
	public String getValue(Player player) {
		return player.getPrayerbook().name();
	}

}
