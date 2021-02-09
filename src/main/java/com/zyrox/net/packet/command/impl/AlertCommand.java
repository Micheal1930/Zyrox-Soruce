package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "alert" }, description = "Sends an alert to every player.")
public class AlertCommand extends Command {
	
	public AlertCommand() {
		super(PlayerRights.ADMINISTRATOR, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}

		World.sendMessage(":alert:" + String.join(" ", Arrays.copyOfRange(args, 0, args.length)));
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::alert [text]"	
		};
	}

}
