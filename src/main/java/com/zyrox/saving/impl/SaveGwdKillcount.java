package com.zyrox.saving.impl;

import com.zyrox.saving.SaveIntArray;
import com.zyrox.world.entity.impl.player.Player;

public class SaveGwdKillcount extends SaveIntArray {

	public SaveGwdKillcount(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] getIntArray(Player player) {
		// TODO Auto-generated method stub
		return player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount();
	}

	@Override
	public void setIntValue(Player player, int index, int value) {
		// TODO Auto-generated method stub
		player.getMinigameAttributes().getGodwarsDungeonAttributes().setKillcount(index,value);
	}



}
