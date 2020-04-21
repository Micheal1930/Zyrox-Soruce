package com.varrock.net.packet.command.impl;

import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.World;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "players" }, description = "Displays the amount of players online.")
public class PlayersCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		DialogueManager.sendStatement(player, "There are currently " + World.getPlayersOnline() + " players online playing Varrock.");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::players"
		};
	}

}
