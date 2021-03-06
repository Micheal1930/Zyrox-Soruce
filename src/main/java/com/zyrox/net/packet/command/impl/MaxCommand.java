package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.combat.CombatFactory;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "max" }, description = "Displays your max hits.")
public class MaxCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}

		int attack = CombatFactory.getMaxHit(player, null, CombatType.MELEE).getDamage() / 10;
		int range = CombatFactory.getMaxHit(player, null, CombatType.RANGED).getDamage() / 10;
		int magic = CombatFactory.getMaxHit(player, null, CombatType.MAGIC).getDamage() / 10;
		player.getPacketSender().sendMessage("</col>Melee attack: @or2@" + attack + "</col>, ranged attack: @or2@" + range + "</col>, magic attack: @or2@" + magic);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::max"
		};
	}

}
