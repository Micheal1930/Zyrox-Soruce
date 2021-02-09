package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

public class SaveOldDonatorPoints extends SaveInteger {

	public SaveOldDonatorPoints(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, int value) {
		player.getPointsHandler().setDonationPoints(value, false);
	}

	@Override
	public Integer getValue(Player player) {
		// TODO Auto-generated method stub
		return player.getPointsHandler().getDonationPoints();
	}

	@Override
	public int getDefaultValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
