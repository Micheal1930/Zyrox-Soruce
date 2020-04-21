package com.varrock.net.packet.command.impl;

import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "setcomp" }, description = "Sets the specified player's account status to 'compromised'.")
public class SetCompCommand extends Command {
	
	public SetCompCommand() {
		super(PlayerRights.ADMINISTRATOR, false);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length == 0) {
			throw new IllegalArgumentException();
		}
		
		String username = String.join(" ", args);
		
		Player p = World.getPlayerByName(username);

		if (p == null) {
			player.sendMessage("That player doesn't seem to be online!");
			return false;
		}
		
		if (p.isAccountCompromised()) {
			player.sendMessage("The player @dre@" + p.getUsername() + " </col>is already compromised.");
			return false;
		}
		
		p.setAccountCompromised(true);
		player.sendMessage("You have successfully set the player's account status to compromised.");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::setcomp [player name]"
		};
	}
	
}
