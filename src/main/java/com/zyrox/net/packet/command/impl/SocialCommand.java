package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.social.SocialCodeManager;
import com.zyrox.world.entity.impl.player.Player;

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
