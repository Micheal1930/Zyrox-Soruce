package com.zyrox.net.packet.command.impl;

import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "spectate" }, description = "Spectate another player")
public class SpectateCommand extends Command {
	
	public SpectateCommand() {
		super(PlayerRights.PLAYER, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length == 0) {
			player.sendMessage("Use format ::spectate [name].");
			return false;
		}
		String username = String.join(" ", args);
		Player other = World.getPlayerByName(username);
		if (other == null) {
			player.sendMessage(username + " was not found to be online.");
			return false;
		}
		if (player.getRegionID() == 12889 || player.getRegionID() == 9043) {
			player.sendMessage("You cannot do this here.");
			return false;
		}
		if (other.getRegionID() != 9043 && other.getRegionID() != 12889) {
			player.sendMessage(username + " is not in the Inferno or Raids.");
			return false;
		}
		player.setSpectateTarget(other);
		player.moveTo(other.getPosition());
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::spectate [playername]"
		};
	}

}
