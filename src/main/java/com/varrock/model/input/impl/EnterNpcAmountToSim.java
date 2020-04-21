package com.varrock.model.input.impl;

import com.varrock.model.input.Input;
import com.varrock.util.Misc;
import com.varrock.world.entity.impl.player.Player;

public class EnterNpcAmountToSim extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if (syntax.length() <= 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		try {
			int amount = Integer.parseInt(syntax);
			if(amount > 10000) {
				amount = 10000;
				player.getPacketSender().sendMessage("Max amount is 10,000!");
			}
			player.npcAmountToSim = amount;
			player.getPacketSender().sendString(72007, ""+amount);
		} catch (Exception e) {
            player.sendMessage("Error! Check your syntax");
        }
	}
}
