package com.varrock.net.packet.command.impl;

import com.varrock.GameSettings;
import com.varrock.model.Item;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "addp"}, description = "Tests adding a item")
public class AddPCommand extends NameCommand {

	public AddPCommand() {
		super("Finch", "Jonny");
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		World.FAKE_PLAYER_COUNT++;
		player.sendMessage(World.FAKE_PLAYER_COUNT);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::addp",
		};
	}

}
