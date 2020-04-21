package com.varrock.saving.impl;

import com.varrock.model.GameMode;
import com.varrock.model.XPMode;
import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

public class SaveXPMode extends SaveString {

	public SaveXPMode(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, String value) {
		player.setXPMode(XPMode.valueOf(value));

	}

	@Override
	public String getValue(Player player) {
		// TODO Auto-generated method stub
		return player.getXPMode().name();
	}

}
