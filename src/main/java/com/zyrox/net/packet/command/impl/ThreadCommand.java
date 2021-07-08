package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "thread" }, description = "Opens the specified forum's thread.")
public class ThreadCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}
		
		String threadId = args[0];
		player.getPacketSender().sendString(1, "https://zyrox.org/forums/"+threadId+"-thread-command/");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::thread [id]"
		};
	}

}
