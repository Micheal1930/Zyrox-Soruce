package com.zyrox.saving.impl;

import com.zyrox.model.Locations.Location;
import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

public class SaveSlayerLocation extends SaveString {

	public SaveSlayerLocation(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		player.getSlayer().setSlayerLocation(Location.valueOf(value));

	}

	@Override
	public String getValue(Player player) {
		return player.getSlayer().getSlayerLocation().name();
	}

}
