package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.entity.impl.player.Player;

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