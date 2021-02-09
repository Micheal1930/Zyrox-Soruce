package com.zyrox.model.input.impl;

import com.zyrox.model.Skill;
import com.zyrox.model.input.EnterAmount;
import com.zyrox.world.entity.impl.player.Player;

public class BuyAgilityExperience extends EnterAmount {

	@Override
	public boolean handleAmount(Player player, int amount) {
		player.getPacketSender().sendInterfaceRemoval();
		int ticketAmount = player.getInventory().getAmount(2996);
		if(ticketAmount == 0) {
			player.getPacketSender().sendMessage("You do not have any tickets.");
			return false;
		}
		if(ticketAmount > amount) {
			ticketAmount = amount;
		}
		
		if(player.getInventory().getAmount(2996) < ticketAmount) {
			return false;
		}
		
		int exp = ticketAmount * 7680;
		player.getInventory().delete(2996, ticketAmount);
		player.getSkillManager().addExperience(Skill.AGILITY, exp);
		player.getPacketSender().sendMessage("You've bought "+exp+" Agility experience for "+ticketAmount+" Agility ticket"+(ticketAmount == 1 ? "" : "s")+".");
		return false;
	}

}
