package com.zyrox.saving.impl;

import com.zyrox.saving.SaveBoolean;
import com.zyrox.world.entity.impl.player.Player;

public class SaveGivenStarter extends SaveBoolean {

	public SaveGivenStarter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.setReceivedStarter(value);
	}

	@Override
	public Boolean getValue(Player player) {
		return player.didReceiveStarter();
	}

	@Override
	public boolean getDefaultValue() {
		// TODO Auto-generated method stub
		return false;
	}


}
