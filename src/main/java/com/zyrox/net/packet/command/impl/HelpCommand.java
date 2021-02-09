package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "help" }, description = "Submits a help request.")
public class HelpCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		if (player.getLastYell().elapsed(300000)) {
            World.sendStaffMessage("<col=FF0066><img=40> [ HELP ]: <col=E8E819><shad=3> " + player.getUsername() + " has requested help!");
            player.getLastYell().reset();
            player.getPacketSender().sendMessage("<col=663300>Your help request has been received. Please be patient.");
        } else {
            player.getPacketSender().sendMessage("").sendMessage("<col=663300>You need to wait 5 Minutes before using this again.").sendMessage("<col=663300>If it's an emergency, please private message a staff member directly instead.");
        }
		
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::help"
		};
	}

}
