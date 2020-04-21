package com.varrock.saving.impl;

import com.varrock.saving.SaveIntArray;
import com.varrock.world.entity.impl.player.Player;

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
