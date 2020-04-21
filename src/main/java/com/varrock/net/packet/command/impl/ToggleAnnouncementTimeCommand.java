package com.varrock.net.packet.command.impl;

import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "toggletime" }, description = "Toggles weather to show announcement time or not.")
public class ToggleAnnouncementTimeCommand extends Command {

	public ToggleAnnouncementTimeCommand() {
		super(PlayerRights.PLAYER, false);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		if(player.isDisplayAnnouncementTimers()) {
			player.setDisplayAnnouncementTimers(false);
			player.sendMessage("@red@Announcement Times: OFF");
		} else {
            player.setDisplayAnnouncementTimers(true);
			player.sendMessage("@red@Announcement Times: ON");
		}
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::toggletime"
		};
	}
	
}
