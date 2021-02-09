package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "coords" }, description = "Prints your current position")
public class CoordsCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		String printedPosition = player.getPosition().toString();
		player.sendMessage(printedPosition);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::coords"
		};
	}

}
