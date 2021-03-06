package com.zyrox.net.packet.command.impl;

import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "uncomp" }, description = "Removes the 'compromised' status from the specified player.")
public class UnCompCommand extends Command {
	
	public UnCompCommand() {
		super(PlayerRights.ADMINISTRATOR, false);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length == 0) {
			throw new IllegalArgumentException();
		}

		String username = String.join(" ", args);
		
		Player p = World.getPlayerByName(username);
		
		if (p != null) {
			p.setAccountCompromised(false);
			player.sendMessage("You have successfully uncompromised the player @dre@" + Misc.formatPlayerName(username) + "</col>.");
			return true;
		}
		
		player.sendMessage("The player @dre@" + Misc.formatPlayerName(username) + " </col>is not compromised.");
		return false;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::uncomp [player name]"
		};
	}

}
