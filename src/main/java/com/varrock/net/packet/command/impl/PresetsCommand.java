package com.varrock.net.packet.command.impl;

import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "presets" }, description = "Opens the presets interface.")
public class PresetsCommand extends Command {
	
	public PresetsCommand() {
		super(PlayerRights.OWNER, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		player.getPresets().open();
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::presets"
		};
	}

}
