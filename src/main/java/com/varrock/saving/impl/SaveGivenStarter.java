package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

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
