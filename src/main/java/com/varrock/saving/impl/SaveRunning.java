package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

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
