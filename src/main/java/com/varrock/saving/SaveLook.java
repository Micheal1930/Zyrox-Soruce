package com.varrock.saving;

import com.varrock.world.entity.impl.player.Player;

public class SaveLook extends SaveIntArray {

	public SaveLook(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] getIntArray(Player player) {
		// TODO Auto-generated method stub
		return player.getAppearance().getLook();
	}

	@Override
	public void setIntValue(Player player, int index, int value) {
		// TODO Auto-generated method stub
		player.getAppearance().set(index, value, false);

	}

}
