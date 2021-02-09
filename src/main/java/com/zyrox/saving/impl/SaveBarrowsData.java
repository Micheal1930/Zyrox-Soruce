package com.zyrox.saving.impl;

import com.zyrox.saving.SaveIntArray;
import com.zyrox.world.entity.impl.player.Player;

public class SaveBarrowsData extends SaveIntArray {

	public SaveBarrowsData(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] getIntArray(Player player) {
		int[][] data = player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData();
		int[] array = new int[data.length];
		for(int i = 0; i < data.length; i++) {
			array[i] = data[i][1];
		}
		// TODO Auto-generated method stub
		return array;
	}

	@Override
	public void setIntValue(Player player, int index, int value) {
		// TODO Auto-generated method stub
		player.getMinigameAttributes().getBarrowsMinigameAttributes().setBarrowsState(index, value);;

	}

}
