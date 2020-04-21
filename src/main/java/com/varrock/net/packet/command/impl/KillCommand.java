package com.varrock.net.packet.command.impl;

import com.varrock.GameSettings;
import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

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
