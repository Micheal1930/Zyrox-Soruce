package com.varrock.saving;

import com.varrock.world.entity.impl.player.Player;

public class SaveCharmImps extends SaveIntArray {

	public SaveCharmImps(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] getIntArray(Player player) {
		// TODO Auto-generated method stub
		return player.getSummoning().getCharmImpConfigs();
	}

	@Override
	public void setIntValue(Player player, int index, int value) {
		// TODO Auto-generated method stub
		player.getSummoning().setCharmImpConfig(index, value);
	}

}
