package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SavePrayerRenewal extends SaveInteger {

	public SavePrayerRenewal(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		player.setPrayerRenewalPotionTimer(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getPrayerRenewalPotionTimer();
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
