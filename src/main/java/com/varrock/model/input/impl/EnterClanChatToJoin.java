package com.varrock.model.input.impl;

import com.varrock.model.input.Input;
import com.varrock.world.content.clan.ClanChatManager;
import com.varrock.world.entity.impl.player.Player;

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
