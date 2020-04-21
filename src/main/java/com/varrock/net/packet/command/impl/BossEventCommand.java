package com.varrock.net.packet.command.impl;

import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "bossevents" }, description = "Displays the time till boss spawns")
public class BossEventCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] { "::bossevents" };
	}

}
