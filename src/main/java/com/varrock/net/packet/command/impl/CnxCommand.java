package com.varrock.net.packet.command.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "cnx" }, description = "Shows the connections.")
public class CnxCommand extends Command {
	
	public CnxCommand() {
		super(PlayerRights.DEVELOPER, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		player.sendMessage("Showing the connections:");

		Map<String, Integer> connections = new HashMap<>();
		
		for (Player plr : World.getPlayers()) {
			if (plr == null) {
				continue;
			}
			
			String ip = plr.getHostAddress();
			
			if (!connections.containsKey(ip)) {
				connections.put(ip, 1);
			} else {
				connections.put(ip, connections.get(ip) + 1);
			}
		}
		
		for (Entry<String, Integer> entry : Misc.entriesSortedByValues(connections, true)) {
			String ip = entry.getKey();
			
			int count = entry.getValue();
			
			if (count > 1) {
				player.sendMessage(count + " from: " + ip);
			}
		}
		
		return false;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::cnx"	
		};
	}

}
