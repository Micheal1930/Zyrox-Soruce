package com.varrock.model.input.impl;

import com.varrock.model.input.Input;
import com.varrock.util.Misc;
import com.varrock.world.content.clan.ClanChatManager;
import com.varrock.world.entity.impl.player.Player;

public class EnterNpcToSim extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if (syntax.length() <= 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		try {
			player.npcToSim = syntax;
			player.getPacketSender().sendString(72005, Misc.capitalize(syntax));
		} catch (Exception e) {
            player.sendMessage("Error! Check your syntax");
        }
	}
}
