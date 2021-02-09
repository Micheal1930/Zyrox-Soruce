package com.zyrox.saving.impl;

import com.zyrox.model.GameMode;
import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

public class SaveGameMode extends SaveString {

	public SaveGameMode(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, String value) {
		player.setGameMode(GameMode.valueOf(value));

	}

	@Override
	public String getValue(Player player) {
		// TODO Auto-generated method stub
		return player.getGameMode().name();
	}

}
