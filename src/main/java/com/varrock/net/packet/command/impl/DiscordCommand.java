package com.varrock.net.packet.command.impl;

import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "discord" }, description = "Opens the link to our discord.")
public class DiscordCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		player.getPA().sendFrame126(12000, "https://discord.gg/S2KnBeM");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::discord"
		};
	}

}
