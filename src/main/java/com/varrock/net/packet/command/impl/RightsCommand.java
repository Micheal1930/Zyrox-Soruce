package com.varrock.net.packet.command.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.varrock.GameServer;
import com.varrock.GameSettings;
import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "rights" }, description = "Sets the rights of a player.")
public class RightsCommand extends NameCommand {
	
	public RightsCommand() {
		super(GameSettings.SPECIAL_STAFF_NAMES, GameSettings.HIGHER_STAFF_NAMES);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length == 0) {
			throw new IllegalArgumentException();
		}
		
		String username = args.length == 1 ? player.getUsername() : String.join(" ", Arrays.copyOfRange(args, 1, args.length));
		
		if (!Misc.isValidName(username)) {
			player.sendMessage("Invalid username.");
			return false;
		}
		
		PlayerRights rights = PlayerRights.forOrdinal(Integer.parseInt(args[0]));
		
		if (rights == null) {
			player.sendMessage("Invalid rights.");
			return false;
		}

		if(!GameServer.isBeta()) {
			if (!GameSettings.SPECIAL_STAFF_NAMES.contains(player.getUsername()) && (rights.equals(PlayerRights.DEVELOPER) || rights.equals(PlayerRights.OWNER))) {
				return false;
			}
		}

		Player p = World.getPlayerByName(username);

		if (p == null) {
			player.sendMessage("That player doesn't seem to be online!");
			return false;
		}
		
		p.setRights(rights);
		p.getPacketSender().sendRights();
		p.save();
		player.sendMessage("You have successfully set the rights of @dre@" + p.getUsername() + "</col> to @dre@" + rights.toString() + "</col>!");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::rights [id]",
			"::rights [id] [player name]"
		};
	}

}
