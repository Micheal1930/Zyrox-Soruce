package com.varrock.saving.impl;

import com.varrock.model.PlayerRelations.PrivateChatStatus;
import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

public class SaveOnlineStatus extends SaveString {

	public SaveOnlineStatus(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, String value) {
		player.getRelations().setStatus(PrivateChatStatus.valueOf(value), false);
	}

	@Override
	public String getValue(Player player) {
		return player.getRelations().getStatus().name();
	}

}
