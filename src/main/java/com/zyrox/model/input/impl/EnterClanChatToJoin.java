package com.zyrox.model.input.impl;

import com.zyrox.model.input.Input;
import com.zyrox.world.content.clan.ClanChatManager;
import com.zyrox.world.entity.impl.player.Player;

public class EnterClanChatToJoin extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if (syntax.length() <= 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		ClanChatManager.join(player, syntax);
	}
}
