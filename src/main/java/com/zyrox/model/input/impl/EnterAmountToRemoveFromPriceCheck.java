package com.zyrox.model.input.impl;

import com.zyrox.model.Item;
import com.zyrox.model.container.impl.PriceChecker;
import com.zyrox.model.input.EnterAmount;
import com.zyrox.world.entity.impl.player.Player;

public class EnterAmountToRemoveFromPriceCheck extends EnterAmount {


	public EnterAmountToRemoveFromPriceCheck(int item, int slot) {
		super(item, slot);
	}

	@Override
	public boolean handleAmount(Player player, int amount) {
		if(!player.getPriceChecker().isOpen() || player.getInterfaceId() != PriceChecker.INTERFACE_ID)
			return false;
		if(!player.getPriceChecker().contains(getItem()))
			return false;
		int invAmount = player.getPriceChecker().getAmount(getItem());
		if(amount > invAmount) 
			amount = invAmount;
		if(amount <= 0)
			return false;
		player.getPriceChecker().switchItem(player.getInventory(), new Item(getItem(), amount), player.getPriceChecker().getSlot(getItem()), false, true);

		return false;
	}
}
