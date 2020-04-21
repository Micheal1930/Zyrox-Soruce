package com.varrock.model.input.impl;

import com.varrock.model.input.EnterAmount;
import com.varrock.world.content.skill.impl.summoning.PouchMaking;
import com.varrock.world.entity.impl.player.Player;

public class EnterAmountToInfuse extends EnterAmount {

	@Override
	public boolean handleAmount(Player player, int amount) {
		if(player.getInterfaceId() != 63471) {
			player.getPacketSender().sendInterfaceRemoval();
			return false;
		}
		PouchMaking.infusePouches(player, amount);

		return false;
	}

}
