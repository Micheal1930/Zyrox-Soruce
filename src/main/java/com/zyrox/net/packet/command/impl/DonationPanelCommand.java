package com.zyrox.net.packet.command.impl;

import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "dpanel" }, description = "Opens the donation panel interface.")
public class DonationPanelCommand extends Command {
	
	public DonationPanelCommand() {
		super(PlayerRights.ADMINISTRATOR, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		player.getDonationPanel().open();
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::dpanel"
		};
	}

}
