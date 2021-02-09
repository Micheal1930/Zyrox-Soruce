package com.zyrox.saving.impl;

import com.zyrox.saving.SaveBoolean;
import com.zyrox.world.entity.impl.player.Player;

public class SaveAutoRetaliate extends SaveBoolean {

	public SaveAutoRetaliate(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.setAutoRetaliate(value);
	}

	@Override
	public Boolean getValue(Player player) {
		return player.isAutoRetaliate();
	}

	@Override
	public boolean getDefaultValue() {
		return false;
	}


}
