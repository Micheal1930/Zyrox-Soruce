package com.varrock.net.packet.command.impl;

import java.util.Calendar;

import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "nextrestart" }, description = "used to check when the next restart is")
public class NextRestartCommand extends NameCommand {
	public NextRestartCommand() {
		super("Finch", "Jonny");
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(World.today);

		int hoursLeft = 24 - cal1.get(Calendar.HOUR_OF_DAY);
		int minutesLeft = 60 - cal1.get(Calendar.MINUTE);


		player.sendMessage("Time tell restart : "+hoursLeft+" hour(s) "+minutesLeft+"minute(s)");

		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] { "::nextrestart" };
	}
}
