package com.varrock.net.packet.command.impl;

import com.varrock.model.Position;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.content.transportation.TeleportHandler;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "gamble" }, description = "Teleports you to the gambling area.")
public class GambleCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		//TODO: Gambling ban
		
		TeleportHandler.teleportPlayer(player, new Position(2440, 3090), player.getSpellbook().getTeleportType());
		player.getPacketSender().sendMessage("@red@Please gamble safely. It is reccomended to record any gambles.");
		player.getPacketSender().sendMessage("@red@YOU MUST HAVE VIDEO EVIDENCE OF GETTING SCAMMED TO FILE A REPORT");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::gamble"
		};
	}

}
