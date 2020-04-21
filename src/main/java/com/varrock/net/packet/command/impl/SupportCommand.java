
package com.varrock.net.packet.command.impl;

import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "support" }, description = "Opens the support page.")
public class SupportCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		player.getPA().sendFrame126("https://varrock.io/support/", 12000);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::support"
		};
	}

}
