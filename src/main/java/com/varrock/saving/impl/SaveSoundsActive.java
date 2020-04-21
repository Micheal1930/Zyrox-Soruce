package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

public class SaveSoundsActive extends SaveBoolean {

	public SaveSoundsActive(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.setSoundsActive(value);
	}

	@Override
	public Boolean getValue(Player player) {
		return player.soundsActive();
	}

	@Override
	public boolean getDefaultValue() {
		return false;
	}


}
