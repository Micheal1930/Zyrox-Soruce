package com.zyrox.net.packet.command.impl;

import com.zyrox.GameSettings;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "home", "edge" }, description = "Teleports you home.")
public class HomeCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		TeleportHandler.teleportPlayer(player, GameSettings.EDGEVILLE, player.getSpellbook().getTeleportType());
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::home", "::edge"
		};
	}

}
