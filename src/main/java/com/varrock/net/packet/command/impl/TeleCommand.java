package com.varrock.net.packet.command.impl;

import com.varrock.model.PlayerRights;
import com.varrock.model.Position;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "tele" }, description = "Teleports you to the specified location.")
public class TeleCommand extends Command {

	public TeleCommand() {
		super(PlayerRights.ADMINISTRATOR, false);
	}
	
	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}
		
		int x = Integer.parseInt(args[0]);
		
		int y = Integer.parseInt(args[1]);
		
		int z = 0;
		
		if (args.length > 2) 
			z = Integer.parseInt(args[2]);
		
		Position position = new Position(x, y, z);
		
		if (!player.isSpecial() && !player.getLocation().canBeMoved(position)) {
			player.sendMessage("You can't teleport from your current location.");
			return false;
		}
		
		player.moveTo(position, true);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::tele [x][y](z)"
		};
	}

}
