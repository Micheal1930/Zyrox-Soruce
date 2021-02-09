package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "removep"}, description = "Tests removing a item")
public class RemovePCommand extends NameCommand {

	public RemovePCommand() {
		super("Finch", "Jonny");
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if(World.FAKE_PLAYER_COUNT <= 0) {
			player.sendMessage("error");
			return true;
		}
		World.FAKE_PLAYER_COUNT--;
		player.sendMessage(World.FAKE_PLAYER_COUNT);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::removep",
		};
	}

}
