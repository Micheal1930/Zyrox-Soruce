package com.zyrox.saving.impl;

import com.zyrox.saving.SaveBoolean;
import com.zyrox.world.entity.impl.player.Player;

public class SaveRunning extends SaveBoolean {

	public SaveRunning(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, boolean value) {
		
	}

	@Override
	public Boolean getValue(Player player) {
		return player.isRunning();
	}

	@Override
	public boolean getDefaultValue() {
		return false;
	}


}
