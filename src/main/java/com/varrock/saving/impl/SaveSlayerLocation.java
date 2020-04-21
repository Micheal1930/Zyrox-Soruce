package com.varrock.saving.impl;

import com.varrock.model.Locations.Location;
import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

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
