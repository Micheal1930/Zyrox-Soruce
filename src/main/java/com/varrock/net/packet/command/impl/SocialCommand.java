package com.varrock.net.packet.command.impl;

import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.content.social.SocialCodeManager;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "social", "code"}, description = "Opens the social code input.")
public class SocialCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		SocialCodeManager.openSocial(player);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::social", "::code"
		};
	}

}
