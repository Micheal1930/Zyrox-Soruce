package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

public class SaveAutoCast extends SaveBoolean {

	public SaveAutoCast(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.setAutocast(value);
	}

	@Override
	public Boolean getValue(Player player) {
		return player.isAutocast();
	}

	@Override
	public boolean getDefaultValue() {
		return false;
	}


}
