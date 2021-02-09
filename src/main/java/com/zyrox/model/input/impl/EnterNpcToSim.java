package com.zyrox.model.input.impl;

import com.zyrox.model.input.Input;
import com.zyrox.util.Misc;
import com.zyrox.world.entity.impl.player.Player;

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
