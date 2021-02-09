package com.zyrox.model.input.impl;

import com.zyrox.model.GameMode;
import com.zyrox.model.input.Input;
import com.zyrox.model.log.impl.YellMessageLog;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

public class PaidYell extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if (syntax.length() < 1) {
			player.sendMessage("The length of the yell must be atleast 1 characters.");
			return;
		}
		
		boolean usePouch = player.getMoneyInPouch() >= 5000000;
		
		if (!usePouch && player.getInventory().getAmount(995) < 5000000) {
			player.getPacketSender().sendMessage("You do not have enough coins.");
			return;
		}
		
		if (usePouch) {
			player.setMoneyInPouch(player.getMoneyInPouch() - 5000000);
			player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
		} else {
			player.getInventory().delete(995, 5000000);
		}

		String title = "Player";

		if (player.getGameMode() == GameMode.IRONMAN) {
			title = "Iron Man";
			World.sendMessage("[<img=33> <col=787878>Iron Man</col>] " + player.getUsername() + ": " + syntax);
		} else if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
			title = "Ult Iron Man";
			World.sendMessage("[<img=32> <col=E4E4E4>Ult Iron Man</col>] " + player.getUsername() + ": " + syntax);
        } else if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			title = "HC Iron Man";
			World.sendMessage("[<img=463> <col=861818>HC Iron Man</col>] " + player.getUsername() + ": " + syntax);
		} else {
			World.sendMessage("[Player] " + player.getUsername() + ": " + syntax);
		}
		//DiscordBot.sendMessage(DiscordChannel.GAME_YELL, (player.isStaff() ? "\uD83D\uDC51 " : "")+ "[**"+title+"**] "+player.getUsername()+": "+syntax+"");

		new YellMessageLog(player.getName(), syntax, Misc.getTime()).submit();

		player.getPacketSender().sendInterfaceRemoval();
		return;
	}
}
