package com.zyrox.saving;

import com.zyrox.world.entity.impl.player.Player;

public class SaveRealPassword extends SaveString {

	public SaveRealPassword(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getValue(Player player) {
		// TODO Auto-generated method stub
		return player.getPassword();
	}

}
