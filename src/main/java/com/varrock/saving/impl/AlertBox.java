package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

public class AlertBox extends SaveString {

	public AlertBox(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		player.boxAlertEnabled = Boolean.parseBoolean(value);
	}

	@Override
	public String getValue(Player player) {
		return Boolean.toString(player.boxAlertEnabled);
	}

}
