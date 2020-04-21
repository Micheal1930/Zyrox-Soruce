package com.varrock.net.packet.command.impl;

import com.varrock.GameServer;
import com.varrock.GameSettings;
import com.varrock.GameType;
import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "flash" }, description = "Move as fast as the flash.")
public class FlashCommand extends Command {

	public FlashCommand() {
		super(PlayerRights.OWNER, false);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		if(GameSettings.GAME_TYPE == GameType.LIVE) {
			return true;
		}

		player.setQuickMovement(!player.isQuickMovement());

		player.sendMessage("You are now moving as fast as "+(player.isQuickMovement() ? "the flash" : "a snail")+"!");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::flash"
		};
	}
	
}
