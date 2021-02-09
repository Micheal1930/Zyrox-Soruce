package com.zyrox.saving.impl;

import com.zyrox.model.Prayerbook;
import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

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
