package com.varrock.model.input.impl;

import com.varrock.model.Item;
import com.varrock.model.input.EnterAmount;
import com.varrock.world.entity.impl.player.Player;

public class EnterAmountToPriceCheck extends EnterAmount {

	public EnterAmountToPriceCheck(int item, int slot) {
		super(item, slot);
	}

	@Override
	public boolean handleAmount(Player player, int amount) {
		if(!player.getPriceChecker().isOpen())
			return false;
		int item = player.getInventory().getItems()[getSlot()].getId();
		if(item != getItem())
			return false;
		int invAmount = player.getInventory().getAmount(item);
		if(amount > invAmount) 
			amount = invAmount;
		if(amount <= 0)
			return false;
		player.getInventory().switchItem(player.getPriceChecker(), new Item(item, amount), getSlot(), false, true);
		return false;
	}
}
