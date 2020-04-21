package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

public class SaveConverted extends SaveBoolean {

	public SaveConverted(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean getValue(Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean getDefaultValue() {
		// TODO Auto-generated method stub
		return false;
	}

}
