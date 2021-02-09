package com.zyrox.saving.impl;

import com.zyrox.saving.SaveString;
import com.zyrox.world.content.skill.impl.slayer.SlayerTasks;
import com.zyrox.world.entity.impl.player.Player;

public class SaveSlayerTask extends SaveString {

	public SaveSlayerTask(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		try {
			player.getSlayer().setSlayerTask(SlayerTasks.valueOf(value));
		} catch (Exception e) {
			player.getSlayer().setSlayerTask(SlayerTasks.NO_TASK);
			System.out.println("User "+player.getName()+" logged in with old slayer task enum... "+value+" setting to NO_TASK");
		}
	}

	@Override
	public String getValue(Player player) {
		return player.getSlayer().getSlayerTask().name();
	}

}
