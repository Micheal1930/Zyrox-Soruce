package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.entity.impl.player.Player;

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
