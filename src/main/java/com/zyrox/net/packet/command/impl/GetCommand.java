package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.GameSettings;
import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "get" }, description = "Gets the specified attribute of a player.")

public class GetCommand extends Command {
	
	public GetCommand() {
		super(PlayerRights.ADMINISTRATOR, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 2) {
			throw new IllegalArgumentException();
		}
		
		String username = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
		
		Player p = World.getPlayerByName(username);
		
		if (p == null) {
			player.sendMessage("Player not found.");
			return false;
		}
		
		if (GameSettings.SPECIAL_STAFF_NAMES.contains(p.getUsername())) {
			player.sendMessage("You can't use this command on that player.");
			return false;
		}
		
		switch(args[0]) {
		case "amt":
			player.sendMessage(Misc.formatPlayerName(username) + " has donated @dre@$" + p.getAmountDonated() + "</col>.");
			break;
		case "dp":
			player.sendMessage(Misc.formatPlayerName(username) + " has @dre@" + p.getPointsHandler().getDonationPoints() + " </col>Donator Points.");
			break;
		case "ip":
			player.sendMessage("The IP of " + Misc.formatPlayerName(username) + " is @dre@" + p.getHostAddress() + " </col>.");
			break;
			
		case "mac":
			player.sendMessage("The mac of " + Misc.formatPlayerName(username) + " is @dre@" + p.getSerialNumber() + " </col>.");
			break;
		case "serial":
			player.sendMessage("The serial of " + Misc.formatPlayerName(username) + " is @dre@" + p.getSuperSerialNumber() + " </col>.");
			break;
		case "pouch":
			player.sendMessage(Misc.formatPlayerName(username) + " has @dre@" + Misc.formatLong(p.getMoneyInPouch())+ " </col>Donator Points.");
			break;
		}
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::get [amt | dp | ip | mac | serial]" 
		};
	}

}
