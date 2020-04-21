package com.varrock.net.packet.command.impl;

import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "location" }, description = "Tells you your location")
public class LocationCommand extends Command {

	public LocationCommand() {
		super(PlayerRights.PLAYER, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		player.sendMessage("Your location ; "+player.getLocation().toString());
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] { "::location" };
	}

}
