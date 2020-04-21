package com.varrock.net.packet.command.impl;

import com.varrock.model.PlayerRights;
import com.varrock.model.Locations.Location;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.World;
import com.varrock.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.varrock.world.content.transportation.TeleportHandler;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "xteleto", "teleto" }, description = "Teleports you to a player.")
public class TeleToCommand extends Command {

	public TeleToCommand() {
		super(PlayerRights.SUPPORT, false);
	}
	
	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}
		
		String username = String.join(" ", args);
		
		Player player2 = World.getPlayerByName(username);
		
		if (player2 == null) {
			player.getPacketSender().sendMessage("Player not found.");
			return false;
		}
		
		if (!player.isSpecial() && (!TeleportHandler.checkReqs(player, player2.getPosition()) || !TeleportHandler.checkXTeleReqs(player, player2.getPosition()))) {
			return false;
		}
		if (player.getLocation().equals(Location.FFAARENA) || player.getLocation().equals(Location.FFALOBBY)) {
			player.getPacketSender().sendMessage("You can't teleport out of FFA.");
			return false;
		}
		if (player2.getLocation().equals(Location.FFAARENA) || player2.getLocation().equals(Location.FFALOBBY)) {
			player.getPacketSender().sendMessage("You can't teleport to a player inside FFA.");
			return false;
		}
		if (Dungeoneering.doingDungeoneering(player2)) {
			player.sendMessage("Rejected teleto for player="+player2.getUsername()+". They are currently in an dungeoneering instance");
			return false;
		}

		if (player2.isInHome()) {
            player.sendMessage("That player is currently in their home and cannot be teleported to, Try ::movehome!");
		    return false;
        }

		player.moveTo(player2.getPosition());
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::xteleto or ::teleto [player name]"
		};
	}

}
