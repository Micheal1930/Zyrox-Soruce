package com.zyrox.saving.impl;

import com.zyrox.saving.SaveBoolean;
import com.zyrox.world.entity.impl.player.Player;

public class SaveMusicActive extends SaveBoolean {

	public SaveMusicActive(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.setMusicActive(value);
	}

	@Override
	public Boolean getValue(Player player) {
		return player.musicActive();
	}

	@Override
	public boolean getDefaultValue() {
		return false;
	}


}
