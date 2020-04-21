package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

public class SaveIsInstanced extends SaveBoolean {

	public SaveIsInstanced(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.setPlayerInstanced(value);
	}

	@Override
	public Boolean getValue(Player player) {
		return player.isPlayerInstanced();
	}

	@Override
	public boolean getDefaultValue() {
		// TODO Auto-generated method stub
		return false;
	}


}
