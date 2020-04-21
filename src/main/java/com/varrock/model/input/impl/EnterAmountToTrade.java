package com.varrock.model.input.impl;

import com.varrock.model.input.EnterAmount;
import com.varrock.world.entity.impl.player.Player;

public class EnterAmountToTrade extends EnterAmount {

	public EnterAmountToTrade(int item, int slot) {
		super(item, slot);
	}
	
	@Override
	public boolean handleAmount(Player player, int amount) {
		if(player.getTrading().inTrade() && getItem() > 0 && getSlot() >= 0 && getSlot() < 28)
			player.getTrading().tradeItem(getItem(), amount, getSlot());
		else
			player.getPacketSender().sendInterfaceRemoval();

		return false;
	}

}
