package com.varrock.net.packet.command.impl;

import com.varrock.GameSettings;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.content.ReferralCode;
import com.varrock.world.content.transportation.TeleportHandler;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "referral",  }, description = "Opens the referral code input.")
public class ReferralCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		ReferralCode.openReferral(player, false);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::referral", "::ref"
		};
	}

}
