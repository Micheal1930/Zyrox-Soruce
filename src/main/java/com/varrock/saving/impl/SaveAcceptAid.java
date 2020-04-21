package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

public class SaveAcceptAid extends SaveBoolean {

	public SaveAcceptAid(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.setAcceptAid(value);
	}

	@Override
	public Boolean getValue(Player player) {
		return player.isAcceptAid();
	}

	@Override
	public boolean getDefaultValue() {
		return false;
	}


}
