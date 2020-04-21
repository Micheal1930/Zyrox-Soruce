package com.varrock.model.input.impl;

import com.varrock.model.Item;
import com.varrock.model.container.impl.BeastOfBurden;
import com.varrock.model.input.EnterAmount;
import com.varrock.world.entity.impl.player.Player;

public class EnterAmountToRemoveFromBob extends EnterAmount {

	public EnterAmountToRemoveFromBob(int item, int slot) {
		super(item, slot);
	}

	@Override
	public boolean handleAmount(Player player, int amount) {
		if(player.getSummoning().getBeastOfBurden() == null || player.getInterfaceId() != BeastOfBurden.INTERFACE_ID)
			return false;
		if(!player.getSummoning().getBeastOfBurden().contains(getItem()))
			return false;
		int invAmount = player.getSummoning().getBeastOfBurden().getAmount(getItem());
		if(amount > invAmount) 
			amount = invAmount;
		if(amount <= 0)
			return false;
		player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), new Item(getItem(), amount), player.getSummoning().getBeastOfBurden().getSlot(getItem()), false, true);

		return false;
	}
}
