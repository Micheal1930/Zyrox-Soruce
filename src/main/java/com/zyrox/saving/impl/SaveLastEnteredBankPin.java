package com.zyrox.saving.impl;

import com.zyrox.saving.SaveLong;
import com.zyrox.world.entity.impl.player.Player;

public class SaveLastEnteredBankPin extends SaveLong {

	public SaveLastEnteredBankPin(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, long value) {
		player.getBankPinAttributes().setLastEnteredBankPin(value);
	}

	@Override
	public Long getValue(Player player) {
		return player.getBankPinAttributes().getLastEnteredBankPin();
	}

	@Override
	public long getDefaultValue() {
		return 0;
	}

}
