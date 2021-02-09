package com.zyrox.net.packet.command.impl;

import com.zyrox.GameSettings;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "update" }, description = "Starts the system update.")
public class UpdateCommand extends NameCommand {

	public UpdateCommand() {
		super(GameSettings.SPECIAL_STAFF_NAMES);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 1) {
			throw new IllegalArgumentException();
		}

		World.savePlayers();

		int time = Integer.parseInt(args[0]);

		World.restart(player, time, true);

		return false;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::update [time]"
		};
	}

}
