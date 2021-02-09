package com.zyrox.net.packet.command.impl;

import com.zyrox.model.PlayerRights;
import com.zyrox.model.Position;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.content.transportation.TeleportType;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "staffzone" }, description = "Teleports you to the staff zone.")

public class StaffzoneCommand extends Command {
	
	public StaffzoneCommand() {
		super(PlayerRights.SUPPORT, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length > 1) {
			throw new IllegalArgumentException();
		}
		

		TeleportHandler.teleportPlayer(player, new Position(2846, 5147), TeleportType.NORMAL);
		
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::staffzone",
			"::staffzone all"
		};
	}

}
