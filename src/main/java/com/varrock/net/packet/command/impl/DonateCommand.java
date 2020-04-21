package com.varrock.net.packet.command.impl;

import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "donate", "store" }, description = "Opens the store page.")
public class DonateCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		player.getPacketSender().sendString(1, "www.varrock.io/store");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::donate", "::store"
		};
	}

}
