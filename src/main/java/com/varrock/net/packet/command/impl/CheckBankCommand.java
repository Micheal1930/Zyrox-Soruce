package com.varrock.net.packet.command.impl;

import java.util.Arrays;

import com.varrock.model.PlayerRights;
import com.varrock.model.container.impl.Bank;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "checkbank" }, description = "Checks the specified player's bank.")
public class CheckBankCommand extends Command {
	
	public CheckBankCommand() {
		super(PlayerRights.ADMINISTRATOR, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}

		String username = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
		
		Player plr = World.getPlayerByName(username);
		
		if (plr == null) {
			player.sendMessage("The player '@dre@" + Misc.formatPlayerName(username) + "</col>' is offline.");
			return false;
		}
		
		player.setAttribute("view_other_bank", plr);
		Bank.viewOther(player, plr, 0);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::checkbank [player name]"	
		};
	}

}
