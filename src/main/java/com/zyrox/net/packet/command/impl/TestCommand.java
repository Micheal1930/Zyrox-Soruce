package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.util.Misc;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "test" }, description = "used to test stuff")
public class TestCommand extends NameCommand {
	public TestCommand() {
		super("Finch", "Jonny");
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {

		int superRareLoot = 0;
		int veryRareLoot = 0;
		int extremeRareLoot = 0;

		for(int i = 0; i < 1000; i++) {
			if (Misc.randomFloat() <= .002) {

				double randomNumber = Misc.randomFloat();
				if (randomNumber < .55) {
					superRareLoot++;
				} else if (randomNumber < .94) {
					veryRareLoot++;
				} else {
					extremeRareLoot++;
				}
			}
		}

		player.sendMessage("Super rare loot = "+superRareLoot+", very rare loot="+veryRareLoot+", extreme rare loot="+extremeRareLoot);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] { "::test" };
	}
}
