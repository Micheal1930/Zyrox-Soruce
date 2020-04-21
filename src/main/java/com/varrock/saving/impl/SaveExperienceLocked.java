package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

public class SaveExperienceLocked extends SaveBoolean {

	public SaveExperienceLocked(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.setExperienceLocked(value);
	}

	@Override
	public Boolean getValue(Player player) {
		return player.experienceLocked();
	}

	@Override
	public boolean getDefaultValue() {
		return false;
	}


}
