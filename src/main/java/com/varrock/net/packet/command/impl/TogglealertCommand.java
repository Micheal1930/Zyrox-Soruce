package com.varrock.net.packet.command.impl;

import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "togglealert" }, description = "Sets the specified player's account status to 'compromised'.")
public class TogglealertCommand extends Command {
	
	public TogglealertCommand() {
		super(PlayerRights.PLAYER, false);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		if(player.boxAlertEnabled == true) {
			player.boxAlertEnabled = false;
			player.sendMessage("Box Alerts: OFF");
		} else {
			player.boxAlertEnabled = true;
			player.sendMessage("Box Alerts: ON");
		}
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::togglealert [player name]"
		};
	}
	
}
