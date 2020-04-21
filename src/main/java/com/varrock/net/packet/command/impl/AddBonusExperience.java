package com.varrock.net.packet.command.impl;

import com.varrock.GameSettings;
import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.World;
import com.varrock.world.content.EffectTimer;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "addbonusxp" }, description = "Increases the bonus xp.")
public class AddBonusExperience extends Command {
	
	public AddBonusExperience() {
		super(PlayerRights.OWNER, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}

		int amount = Integer.parseInt(args[0]);
		
		if (amount < 0 || amount > 1440) {
			player.sendMessage("The amount must be between 0 and 1440.");
			return true;
		}
		
		GameSettings.incrementBonusXp(amount);
		
		World.getPlayers().forEach(p -> p.getPacketSender().sendEffectTimer((int) GameSettings.getBonusXpSecondsLeft(), EffectTimer.EXPERIENCE));

		player.sendMessage("Successfully increased the bonus experience by: @dre@" + amount + "</col> minutes.");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::addbonusxp [amount in minutes]"	
		};
	}

}
