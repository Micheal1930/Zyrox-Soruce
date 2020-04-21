package com.varrock.saving;

import com.varrock.world.entity.impl.player.Player;

public abstract class SaveBooleanArray extends SaveIntArray {

	public SaveBooleanArray(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] getIntArray(Player player) {
		// TODO Auto-generated method stub
		boolean[] boolArray = getBooleanArray(player);
		int[] intArray = new int[boolArray.length];
		for(int i = 0; i < boolArray.length; i++) {
			intArray[i] = boolArray[i] ? 1: 0;
		}
		return intArray;
	}

	@Override
	public void setIntValue(Player player, int index, int value) {
		setBooleanValue(player,index, value > 0);
	}
	
	public abstract boolean[] getBooleanArray(Player player);
	
	public abstract void setBooleanValue(Player player, int index, boolean value);

}
