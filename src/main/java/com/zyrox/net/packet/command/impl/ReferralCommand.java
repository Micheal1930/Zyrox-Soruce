package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.ReferralCode;
import com.zyrox.world.entity.impl.player.Player;

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
