package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.World;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.entity.impl.player.Player;

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
