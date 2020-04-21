package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

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
