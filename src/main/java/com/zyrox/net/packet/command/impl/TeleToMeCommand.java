package com.zyrox.net.packet.command.impl;

import com.zyrox.model.PlayerRights;
import com.zyrox.model.Locations.Location;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.World;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.content.transportation.TeleportType;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "xteletome", "teletome" }, description = "Teleports a player to you.")
public class TeleToMeCommand extends Command {

	public TeleToMeCommand() {
		super(PlayerRights.MODERATOR, false);
	}
	
	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}
		
		if (!player.getRights().isHighStaff() && player.getWildernessLevel() > 0) {
			player.sendMessage("You can't use this command right now.");
			return false;
		}
		
		String username = String.join(" ", args);
		
		Player player2 = World.getPlayerByName(username);
		
		if (player2 == null) {
			player.getPacketSender().sendMessage("Player not found.");
			return false;
		}
		
		boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy()) && player.getRegionInstance() == null && player2.getRegionInstance() == null || player.getRights().isHighStaff();
		if (canTele && !TeleportHandler.checkXTeleReqs(player, player2.getPosition())) {
			canTele = false;
		}
			if (!canTele && player.isSpecial()) {
				canTele = true;
			}
		if (!canTele || player2.getLocation().equals(Location.FFAARENA) || player2.getLocation().equals(Location.FFALOBBY)) {
			player.getPacketSender().sendMessage("You can't teleport that player at the moment.");
			return false;
		}

        if (player2.isInHome()) {
            player.sendMessage("That player is currently in their home and cannot be teleported to, Try ::movehome!");
            return false;
        }
		
		TeleportHandler.teleportPlayer(player2, player.getPosition().copy(), TeleportType.NORMAL);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::xteletome or ::teleto [player name]"
		};
	}

}
