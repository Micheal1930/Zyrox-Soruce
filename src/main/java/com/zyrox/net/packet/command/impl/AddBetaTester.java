package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.GameServer;
import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.BetaTesters;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "addtester" }, description = "Adds a beta tester.")

public class AddBetaTester extends Command {

	public AddBetaTester() {
		super(PlayerRights.ADMINISTRATOR, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}

		if(!GameServer.isBeta()) {
			return true;
		}

		String username = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
		
		BetaTesters.addBetaTester(username);

		player.sendMessage("Added " + username + " to the beta tester list.");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::addtester [name]",
		};
	}

}
