package com.zyrox.model.input.impl;

import com.zyrox.model.input.EnterAmount;
import com.zyrox.world.content.skill.impl.summoning.PouchMaking;
import com.zyrox.world.entity.impl.player.Player;

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
