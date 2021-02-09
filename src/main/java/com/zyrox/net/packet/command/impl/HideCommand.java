package com.zyrox.net.packet.command.impl;

import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "hide" }, description = "Hides yourself from others.")
public class HideCommand extends Command {
	
	public HideCommand() {
		super(PlayerRights.ADMINISTRATOR, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		if(player.isHidePlayer()){
			player.setHidePlayer(false);		
			player.getPA().sendMessage("You are now visible again.");
			return true;
		} else {
		player.setHidePlayer(true);		
		player.getPA().sendMessage("You are now completely invisible to other players. Relog to become visible");
		return true;
		}
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::hide"
		};
	}

}
