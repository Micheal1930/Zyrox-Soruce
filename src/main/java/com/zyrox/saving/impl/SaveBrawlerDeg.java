package com.zyrox.saving.impl;

import com.zyrox.saving.SaveIntArray;
import com.zyrox.world.entity.impl.player.Player;

public class SaveBrawlerDeg extends SaveIntArray {

	public SaveBrawlerDeg(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] getIntArray(Player player) {
		// TODO Auto-generated method stub
		return player.getBrawlerChargers();
	}

	@Override
	public void setIntValue(Player player, int index, int value) {
		// TODO Auto-generated method stub
		player.setBrawlerCharge(index,value);
	}

}
