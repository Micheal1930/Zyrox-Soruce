package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.content.LoyaltyProgramme.LoyaltyTitles;
import com.varrock.world.entity.impl.player.Player;

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
