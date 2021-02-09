package com.zyrox.saving.impl;

import com.zyrox.saving.SaveString;
import com.zyrox.world.content.skill.impl.slayer.SlayerMaster;
import com.zyrox.world.entity.impl.player.Player;

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
