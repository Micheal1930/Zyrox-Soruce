package com.zyrox.saving.impl;

import com.zyrox.saving.SaveString;
import com.zyrox.world.content.skill.impl.slayer.SlayerTasks;
import com.zyrox.world.entity.impl.player.Player;

public class SavePrevSlayerTask extends SaveString {

	public SavePrevSlayerTask(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		try {
			player.getSlayer().setLastTask(SlayerTasks.valueOf(value));
		} catch (Exception e) {
			player.getSlayer().setLastTask(SlayerTasks.NO_TASK);
			System.out.println("User "+player.getName()+" logged in with old slayer task enum... "+value+" setting to NO_TASK");
		}
	}

	@Override
	public String getValue(Player player) {
		return player.getSlayer().getLastTask().name();
	}

}
