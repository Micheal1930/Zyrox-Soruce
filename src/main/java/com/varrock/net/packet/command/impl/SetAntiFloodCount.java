package com.varrock.net.packet.command.impl;

import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "setfloodcount"}, description = "Sets the flood count")
public class SetAntiFloodCount extends NameCommand {

	public SetAntiFloodCount() {
		super("Finch", "Jonny", "Nick", "Master cut22", "Sly");
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length == 0) {
			throw new IllegalArgumentException();
		}
		World.ANTI_FLOOD_COUNT = Integer.parseInt(args[0]);
		player.sendMessage("Set the anti flood count to: "+World.ANTI_FLOOD_COUNT);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::setfloodcount",
		};
	}

}
