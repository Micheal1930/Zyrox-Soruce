package com.zyrox.saving.impl;

import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

public class SaveDuoPartner extends SaveString {

	public SaveDuoPartner(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		player.getSlayer().setDuoPartner(value.equals("null") ? null : value);
	}

	@Override
	public String getValue(Player player) {
		return player.getSlayer().getDuoPartner() == null ? "null" : player.getSlayer().getDuoPartner();
	}

}
