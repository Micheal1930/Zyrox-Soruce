package com.zyrox.saving.impl;

import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

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
