package com.zyrox.saving.impl;

import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

public class EliteClueScrollStep extends SaveString {

	public EliteClueScrollStep(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		player.eliteClueSteps = Integer.parseInt(value);
	}

	@Override
	public String getValue(Player player) {
		return Integer.toString(player.eliteClueSteps);
	}

}
