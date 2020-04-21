package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

public class SaveSalt extends SaveString {

	public SaveSalt(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, String value) {
		player.getPasswordNew().setSalt(value);
	}

	@Override
	public String getValue(Player player) {
		if(player.getPasswordNew().getSalt() == null)
			return "null";
		return player.getPasswordNew().getSalt();//return player.getPassword().getSalt();
	}

}
