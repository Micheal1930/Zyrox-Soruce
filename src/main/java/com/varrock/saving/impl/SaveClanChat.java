package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

public class SaveClanChat extends SaveString {

	public SaveClanChat(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		if (!value.equals("null"))
			player.setClanChatName(value);
	}

	@Override
	public String getValue(Player player) {
		return player.getClanChatName() == null ? "null" : player.getClanChatName().trim();
	}

}
