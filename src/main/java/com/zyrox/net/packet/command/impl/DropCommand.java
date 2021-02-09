package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.model.definitions.NpcDefinition;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.dropchecker.NPCDropTableChecker;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = {"drops"}, description = "Displays the drops interface.")
public class DropCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {

		if (args.length < 1) {
			NPCDropTableChecker.getSingleton().showNPCDropTable(player,
					NPCDropTableChecker.getSingleton().dropTableNpcIds.get(0));
			return true;
		}

		NPCDropTableChecker.getSingleton().refreshDropTableChilds(player);

		String name = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

		for (NpcDefinition def : NpcDefinition.getDefinitions()) {

			if (def == null || def.getName() == null) {
				continue;
			}

			if (def.getName().toLowerCase().contains(name) && NPCDropTableChecker.getSingleton().dropTableNpcNames.contains(def.getName())) {
				NPCDropTableChecker.getSingleton().showNPCDropTable(player, def.getId());
				return true;
			}

		}
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] { "::drops" };
	}

}
