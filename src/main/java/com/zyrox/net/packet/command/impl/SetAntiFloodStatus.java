package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "setfloodstatus"}, description = "Sets the flood status")
public class SetAntiFloodStatus extends NameCommand {

	public SetAntiFloodStatus() {
		super("Finch", "Jonny", "Nick", "Master cut22", "Sly");
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length == 0) {
			throw new IllegalArgumentException();
		}
		World.ANTI_FLOOD = !World.ANTI_FLOOD;
		player.sendMessage("Set the anti flood status to: "+World.ANTI_FLOOD);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::setfloodstatus",
		};
	}

}
