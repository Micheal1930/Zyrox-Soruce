
package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "benefits" , "benefit"}, description = "Opens the donator benefits")
public class BenefitsCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		player.getPA().sendFrame126("https://zyrox.org/store", 12000);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::benefits"
		};
	}

}
