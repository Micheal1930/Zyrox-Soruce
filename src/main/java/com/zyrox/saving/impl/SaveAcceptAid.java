package com.zyrox.saving.impl;

import com.zyrox.saving.SaveBoolean;
import com.zyrox.world.entity.impl.player.Player;

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
