package com.zyrox.saving.impl;

import com.zyrox.saving.SaveBoolean;
import com.zyrox.world.entity.impl.player.Player;

public class SaveDoubleSlayerXp extends SaveBoolean {

	public SaveDoubleSlayerXp(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.getSlayer().doubleSlayerXP = value;
	}

	@Override
	public Boolean getValue(Player player) {
		return player.getSlayer().doubleSlayerXP;
	}

	@Override
	public boolean getDefaultValue() {
		return false;
	}


}
