package com.varrock.net.packet.command.impl;

import java.util.Arrays;

import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "setyelltitle" }, description = "sets yell title")
public class SetYellTitleCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		
		if(player.getAmountDonated() < 500) {
			player.getPacketSender().sendMessage("You haven't donated enough to set a yell title. The requirement is Extreme donator.");
			return true;
		}

		String title = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

		player.yellTitle = title;
		player.getPacketSender().sendMessage("Your yell title has been set to: "+title);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] { "::yell [message]" };
	}

}