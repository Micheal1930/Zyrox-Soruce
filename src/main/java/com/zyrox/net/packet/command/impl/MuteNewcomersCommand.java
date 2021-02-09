package com.zyrox.net.packet.command.impl;

import com.zyrox.GameSettings;
import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "mutenewcomers" }, description = "Toggles the mute newcomers setting.")
public class MuteNewcomersCommand extends Command {

	public MuteNewcomersCommand() {
		super(PlayerRights.MODERATOR, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}

		GameSettings.MUTE_NEWCOMERS = !GameSettings.MUTE_NEWCOMERS;
		
		if (GameSettings.MUTE_NEWCOMERS) {
			player.sendMessage("Newcomers are now muted until 5 minutes of gameplay.");
		} else {
			player.sendMessage("Newcomers are no longer muted.");
		}
		
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::mutenewcomers"
		};
	}

}
