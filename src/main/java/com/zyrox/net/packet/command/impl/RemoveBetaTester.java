package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.BetaTesters;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "removetester" }, description = "Removes a beta tester.")

public class RemoveBetaTester extends Command {

	public RemoveBetaTester() {
		super(PlayerRights.ADMINISTRATOR, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}

		String username = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
		
		BetaTesters.removeBetaTester(username);

		player.sendMessage("Removed " + username + " from the beta tester list.");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::removetester [name]",
		};
	}

}
