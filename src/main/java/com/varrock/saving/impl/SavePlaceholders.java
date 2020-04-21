package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

public class SavePlaceholders extends SaveBoolean {

	public SavePlaceholders(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.setPlaceholders(value);
	}

	@Override
	public Boolean getValue(Player player) {
		return player.isPlaceholders();
	}

	@Override
	public boolean getDefaultValue() {
		return false;
	}

}
