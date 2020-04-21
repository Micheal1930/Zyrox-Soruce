
package com.varrock.net.packet.command.impl;

import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "hiscores", "scores", "highscores" }, description = "Opens the highscores page.")
public class HiscoresCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		player.getPA().sendFrame126("http://varrock.io/hiscores", 12000);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::hiscores"
		};
	}

}
