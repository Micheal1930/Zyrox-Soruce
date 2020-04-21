package com.varrock.net.packet.command.impl;

import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "updates" }, description = "Opens the updates section on the forums.")
public class UpdatesCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		player.getPacketSender().sendString(1, "https://varrock.io/forums/");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::updates"
		};
	}

}
