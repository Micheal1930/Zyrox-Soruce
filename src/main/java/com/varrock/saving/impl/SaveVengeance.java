package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

public class SaveVengeance extends SaveBoolean {

	public SaveVengeance(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.setHasVengeance(value);
	}

	@Override
	public Boolean getValue(Player player) {
		return player.hasVengeance();
	}

	@Override
	public boolean getDefaultValue() {
		return false;
	}


}
