package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.entity.impl.bot.BotManager;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "addbot" }, description = "Adds a bot with the specified name.")
public class AddBotCommand extends Command {
	
	public AddBotCommand() {
		super(PlayerRights.DEVELOPER, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}

		String username = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
		
		BotManager.addBot("afk", username, player.getPosition());
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::addbot [player name]"	
		};
	}

}
