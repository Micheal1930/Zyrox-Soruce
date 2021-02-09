package com.zyrox.saving.impl;

import com.zyrox.saving.SaveBoolean;
import com.zyrox.world.entity.impl.player.Player;

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
