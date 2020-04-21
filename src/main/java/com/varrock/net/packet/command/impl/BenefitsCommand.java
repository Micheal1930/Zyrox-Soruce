
package com.varrock.net.packet.command.impl;

import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "benefits" , "benefit"}, description = "Opens the donator benefits")
public class BenefitsCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		player.getPA().sendFrame126("https://varrock.io/store", 12000);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::benefits"
		};
	}

}
