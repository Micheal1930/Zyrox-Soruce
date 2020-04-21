package com.varrock.net.packet.command.impl;

import com.varrock.GameSettings;
import com.varrock.model.definitions.NpcDefinition;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "getnpc" }, description = "Get an NPC ID by its name.")
public class GetNpcCommand extends NameCommand {

	public GetNpcCommand() {
		super(GameSettings.SPECIAL_STAFF_NAMES, GameSettings.DONATION_PUSHERS);
	}

	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length == 0) {
			throw new IllegalArgumentException();
		}

		String npcName = String.join(" ", args);

		int i = 0;

		for (NpcDefinition definition : NpcDefinition.getDefinitions()) {
			if (definition == null) {
				continue;
			}

			if (definition.getName() != null && definition.getName().toLowerCase().contains(npcName)) {
				player.sendMessage(definition.getName() + " @dre@- </col>" + definition.getId());

				if (i++ == 100) {
					player.sendMessage("Over @dre@100 </col>results have been found!");
					break;
				}
			}
		}

		if (i == 0) {
			player.sendMessage("No results for `@red@" + npcName + "</col>`.");
		}

		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::getnpc [npc name]"	
		};
	}

}
