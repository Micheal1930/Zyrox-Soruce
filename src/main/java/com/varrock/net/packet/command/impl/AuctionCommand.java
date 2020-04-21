package com.varrock.net.packet.command.impl;

import com.varrock.GameSettings;
import com.varrock.model.Position;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.content.transportation.TeleportHandler;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "auction" }, description = "Teleports you to the auction house.")
public class AuctionCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		TeleportHandler.teleportPlayer(player, new Position(3104, 3501, 0), player.getSpellbook().getTeleportType());
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::auction"
		};
	}

}
