package com.zyrox.saving.impl;

import com.zyrox.saving.SaveBoolean;
import com.zyrox.world.entity.impl.player.Player;

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
