package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "updates" }, description = "Opens the updates section on the forums.")
public class UpdatesCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		player.getPacketSender().sendString(1, "https://zyrox.org/forums/");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::updates"
		};
	}

}
