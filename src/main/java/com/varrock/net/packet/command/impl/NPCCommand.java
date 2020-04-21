package com.varrock.net.packet.command.impl;

import com.varrock.GameSettings;
import com.varrock.model.*;
import com.varrock.net.packet.command.*;
import com.varrock.world.World;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "npc" }, description = "Spawns an NPC")
public class NPCCommand extends NameCommand {

	public NPCCommand() {
		super(GameSettings.SPECIAL_STAFF_NAMES, GameSettings.HIGHER_STAFF_NAMES);
	}
	
	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		try {
			int npcId = Integer.valueOf(args[0]);
			
			boolean osrs = false;
			
			if (args.length > 1) {
				osrs = Boolean.parseBoolean(args[1]);
			}
			
			NPC n = NPC.of(osrs ? GameSettings.OSRS_NPC_OFFSET + npcId : npcId, new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()));
			World.register(n);
			
			if (osrs) {
				player.sendMessage("Spawning OSRS npc: " + npcId);
			} else {
				player.sendMessage("Spawning npc: " + npcId);
			}
			
		} catch (Exception e) {
			player.sendMessage("Error! try using ::npc id");
		}
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::npc"
		};
	}

}
