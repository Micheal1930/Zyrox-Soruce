package com.zyrox.net.packet.command.impl;

import com.zyrox.GameSettings;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "disablespawn" }, description = "Teleports you home.")
public class DisableSpawnCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 1) {
			throw new IllegalArgumentException();
		}
		
		boolean input = Boolean.valueOf(args[1]);
		
		GameSettings.SPAWN_ENABLED = Boolean.valueOf(input);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::disablespawn [true/false]"
		};
	}

}
