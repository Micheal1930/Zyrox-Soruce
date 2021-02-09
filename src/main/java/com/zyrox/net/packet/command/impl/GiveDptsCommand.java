package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.GameSettings;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.saving.PlayerSaving;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "givedpoints" }, description = "gives donator points to  the specified player.")
public class GiveDptsCommand extends NameCommand {
	
	public GiveDptsCommand() {
		super(GameSettings.SPECIAL_STAFF_NAMES, GameSettings.HIGHER_STAFF_NAMES);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		
		if (args.length < 2) {
			throw new IllegalArgumentException();
		}
		
		int amount = Integer.parseInt(args[0]);
		
		int from = 3;
		
		
		String username = String.join(" ", Arrays.copyOfRange(args, args.length <= 2 ? 1 : from, args.length));
		
		if (!PlayerSaving.getSaving().exists(Misc.formatPlayerName(username))) {
			player.sendMessage("A player with that name does not exist.");
			return false;
		}
		
		
		Player plr = World.getPlayerByName(username);
		
		if (plr == null) {
			player.sendMessage("The player '@dre@" + Misc.formatPlayerName(username) + "</col>' is offline.");
			return false;
		}
		
		plr.getPointsHandler().setDonationPoints(amount, true);
		player.sendMessage("Successfully gave </col>(@dre@" + amount + "</col>) x @dre@Donator Points </col>to " + Misc.formatPlayerName(username) + ".");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::givedpoints [amount] [username]"
		};
	}

}
