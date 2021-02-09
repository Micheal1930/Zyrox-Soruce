package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

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
