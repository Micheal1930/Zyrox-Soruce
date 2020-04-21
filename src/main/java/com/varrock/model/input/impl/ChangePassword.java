package com.varrock.model.input.impl;

import com.varrock.GameSettings;
import com.varrock.model.input.Input;
import com.varrock.util.NameUtils;
import com.varrock.world.entity.impl.player.Player;

public class ChangePassword extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendInterfaceRemoval();

		if (!GameSettings.MYSQL_ENABLED) {
			player.getPacketSender().sendMessage("This service is currently unavailable.");
			return;
		}

		if (syntax == null || syntax.length() <= 2 || syntax.length() > 15 || !NameUtils.isValidName(syntax)) {
			player.getPacketSender().sendMessage("That password is invalid. Please try another password.");
			return;
		}
		
		if (syntax.contains("_")) {
			player.getPacketSender().sendMessage("Your password can not contain underscores.");
			return;
		}
		
		if (player.requiresUnlocking()) {
			player.getPacketSender().sendMessage("Please visit the nearest bank and enter your pin before doing this.");
			return;
		}

		player.setPassword(syntax);
		player.getPasswordNew().setRealPassword(syntax);
		player.getPacketSender().sendMessage("Your account's password is now: " + syntax);

	}
}
