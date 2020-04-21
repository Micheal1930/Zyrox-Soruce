package com.varrock.saving;

import com.varrock.world.entity.impl.player.Player;

public class SaveCompCapeColours extends SaveIntArray {

	public SaveCompCapeColours(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] getIntArray(Player player) {
		// TODO Auto-generated method stub
		return player.getCompCapeColors();
	}

	@Override
	public void setIntValue(Player player, int index, int value) {
		// TODO Auto-generated method stub
		player.setCompCapeColor(index,value);
	}

}
