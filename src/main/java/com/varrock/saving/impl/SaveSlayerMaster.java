package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.content.skill.impl.slayer.SlayerMaster;
import com.varrock.world.entity.impl.player.Player;

public class SaveSlayerMaster extends SaveString {

	public SaveSlayerMaster(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		player.getSlayer().setSlayerMaster(SlayerMaster.valueOf(value));
	}

	@Override
	public String getValue(Player player) {
		return player.getSlayer().getSlayerMaster().name();
	}

}
