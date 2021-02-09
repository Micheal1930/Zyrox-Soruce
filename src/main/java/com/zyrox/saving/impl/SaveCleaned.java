package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveCleaned extends SaveInteger {

	public SaveCleaned(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, int value) {
		// TODO Auto-generated method stub
		//player.sendMessage("SaveCleaned obj: " + value);
		player.setCleaned(value);
	}

	@Override
	public Integer getValue(Player player) {
		// TODO Auto-generated method stub
		return player.getCleaned();
	}

	@Override
	public int getDefaultValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
