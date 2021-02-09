package com.zyrox.net.packet.command.impl;

import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "kill-server" }, description = "Starts the system update.")
public class KillCommand extends Command {

	public KillCommand() {
		super(PlayerRights.ADMINISTRATOR, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {

		World.savePlayers();

		World.restart(player, -1, true);

		return false;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::kill-server"
		};
	}

}
