package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.content.skill.impl.slayer.KonarQuoMaten.SlayerTask;
import com.varrock.world.entity.impl.player.Player;

public class SaveSlayerTask2 extends SaveString {

	public SaveSlayerTask2(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		player.getSlayer().setTask(SlayerTask.valueOf(value));
	}

	@Override
	public String getValue(Player player) {
		return player.getSlayer().getTask().name();
	}

}
