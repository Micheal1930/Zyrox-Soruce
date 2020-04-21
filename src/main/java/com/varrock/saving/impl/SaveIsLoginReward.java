package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

public class SaveIsLoginReward extends SaveBoolean {

	public SaveIsLoginReward(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.setLoginReward(value);
	}

	@Override
	public Boolean getValue(Player player) {
		return player.isLoginReward();
	}

	@Override
	public boolean getDefaultValue() {
		return false;
	}


}
