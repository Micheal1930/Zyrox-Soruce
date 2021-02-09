package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.model.PlayerRights;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.ReferralCode;
import com.zyrox.world.content.social.SocialCodeManager;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "reload" }, description = "Reloads data.")

public class ReloadCommand extends Command {

	public ReloadCommand() {
		super(PlayerRights.ADMINISTRATOR, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}

		String type = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
		
		switch(type.toLowerCase()) {
			case "referral":
			case "referrals":
				ReferralCode.load();
				break;
			case "social":
			case "socials":
				SocialCodeManager.load();
				break;
			case "items":
				ItemDefinition.init();
				break;
		}

		player.sendMessage("Reloaded...");

		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::reload [type]",
		};
	}

}
