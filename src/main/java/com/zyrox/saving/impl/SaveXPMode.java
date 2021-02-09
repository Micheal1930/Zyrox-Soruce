package com.zyrox.saving.impl;

import com.zyrox.model.XPMode;
import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

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
