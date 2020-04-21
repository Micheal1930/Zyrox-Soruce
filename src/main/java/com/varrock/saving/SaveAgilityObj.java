package com.varrock.saving;

import com.varrock.world.entity.impl.player.Player;

public class SaveAgilityObj extends SaveBooleanArray {

	public SaveAgilityObj(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean[] getBooleanArray(Player player) {
		// TODO Auto-generated method stub
		return player.getCrossedObstacles();
	}

	@Override
	public void setBooleanValue(Player player, int index, boolean value) {
		player.setCrossedObstacle(index, value);

	}

}
