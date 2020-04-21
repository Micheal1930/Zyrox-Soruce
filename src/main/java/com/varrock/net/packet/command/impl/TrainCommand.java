package com.varrock.net.packet.command.impl;

import com.varrock.model.Position;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.content.transportation.TeleportHandler;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "train" }, description = "Teleports you to rock crabs.")
public class TrainCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		TeleportHandler.teleportPlayer(player, new Position(2679, 3714), player.getSpellbook().getTeleportType());
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::train"
		};
	}

}
