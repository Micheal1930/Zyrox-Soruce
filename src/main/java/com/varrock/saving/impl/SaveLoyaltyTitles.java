package com.varrock.saving.impl;

import com.varrock.saving.SaveBooleanArray;
import com.varrock.world.entity.impl.player.Player;

public class SaveLoyaltyTitles extends SaveBooleanArray {

	public SaveLoyaltyTitles(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean[] getBooleanArray(Player player) {
		// TODO Auto-generated method stub
		return player.getUnlockedLoyaltyTitles();
	}

	@Override
	public void setBooleanValue(Player player, int index, boolean value) {
		// TODO Auto-generated method stub
		if(value) {
			player.setUnlockedLoyaltyTitle(index);
		}
	}

}
