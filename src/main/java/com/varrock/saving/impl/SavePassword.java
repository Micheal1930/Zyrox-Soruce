package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

public class SavePassword extends SaveString {

	public SavePassword(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, String value) {
		//player.setPassword(value);
		//player.getPasswordNew().setEncryptedPass(value);
	}

	@Override
	public String getValue(Player player) {
		if(player.getDatabaseId() == 1) {
			//System.out.println("Getting savePassword: " + player.getPasswordNew().getEncryptedPass());
		}
		if(player.getPasswordNew().getEncryptedPass() != null)
			return player.getPasswordNew().getEncryptedPass();
		return "null";
	}

}
