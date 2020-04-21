package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

public class SaveAccountCompromised extends SaveBoolean {

	public SaveAccountCompromised(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.setAccountCompromised(value);
	}

	@Override
	public Boolean getValue(Player player) {
		return player.isAccountCompromised();
	}

	@Override
	public boolean getDefaultValue() {
		return false;
	}


}
