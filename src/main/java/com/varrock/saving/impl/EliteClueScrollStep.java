package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

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
