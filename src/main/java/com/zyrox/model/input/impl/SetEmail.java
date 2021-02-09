package com.zyrox.model.input.impl;

import com.zyrox.GameSettings;
import com.zyrox.model.input.Input;
import com.zyrox.world.content.Achievements;
import com.zyrox.world.content.PlayerPanel;
import com.zyrox.world.content.Achievements.AchievementData;
import com.zyrox.world.entity.impl.player.Player;

public class SetEmail extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendInterfaceRemoval();
		
		if (syntax.length() <= 3 || !syntax.contains("@") || syntax.endsWith("@")) {
			player.getPacketSender().sendMessage("Invalid email, please enter a valid one.");
			return;
		}
		
		if (!GameSettings.MYSQL_ENABLED) {
			player.getPacketSender().sendMessage("This service is currently unavailable.");
			return;
		}
		
		if (player.requiresUnlocking()) {
			player.getPacketSender().sendMessage("Please visit the nearest bank and enter your pin before doing this.");
			return;
		}

		if (player.getEmailAddress() != null && syntax.equalsIgnoreCase(player.getEmailAddress())) {
			player.getPacketSender().sendMessage("This is already your email-address!");
			return;
		}

		player.setEmailAddress(syntax);
		player.getPacketSender().sendMessage("Your account's email-adress is now: " + syntax);
		Achievements.finishAchievement(player, AchievementData.SET_AN_EMAIL_ADDRESS);
		PlayerPanel.refreshPanel(player);

	}
}
