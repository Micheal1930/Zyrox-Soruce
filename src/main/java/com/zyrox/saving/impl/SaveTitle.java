package com.zyrox.saving.impl;

import com.zyrox.saving.SaveString;
import com.zyrox.world.content.LoyaltyProgramme.LoyaltyTitles;
import com.zyrox.world.entity.impl.player.Player;

public class SaveTitle extends SaveString {

	public SaveTitle(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, String value) {
		player.setLoyaltyTitle(LoyaltyTitles.valueOf(value));
	}

	@Override
	public String getValue(Player player) {
		return player.getLoyaltyTitle().name();
	}

}
