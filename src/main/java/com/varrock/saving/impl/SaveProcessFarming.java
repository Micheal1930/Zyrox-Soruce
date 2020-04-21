package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

public class SaveProcessFarming extends SaveBoolean {

	public SaveProcessFarming(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.setProcessFarming(value);
	}

	@Override
	public Boolean getValue(Player player) {
		return player.shouldProcessFarming();
	}

	@Override
	public boolean getDefaultValue() {
		return false;
	}


}
