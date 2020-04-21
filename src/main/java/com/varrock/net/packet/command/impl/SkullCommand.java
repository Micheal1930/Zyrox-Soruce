package com.varrock.net.packet.command.impl;

import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.content.combat.CombatFactory;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "skull" }, description = "Toggles your skull.")

public class SkullCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (player.getSkullTimer() > 0) {
			player.sendMessage("@red@You are already skulled!");
			return false;
		}
		
		CombatFactory.skullPlayer(player);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::skull" 
		};
	}

}
