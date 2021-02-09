package com.zyrox.model.input.impl;

import com.zyrox.model.Item;
import com.zyrox.model.container.impl.BeastOfBurden;
import com.zyrox.model.input.EnterAmount;
import com.zyrox.world.entity.impl.player.Player;

public class EnterAmountToStore extends EnterAmount {

	public EnterAmountToStore(int item, int slot) {
		super(item, slot);
	}

	@Override
	public boolean handleAmount(Player player, int amount) {
		if(player.getSummoning().getBeastOfBurden() == null || player.getInterfaceId() != BeastOfBurden.INTERFACE_ID)
			return false;
		int item = player.getInventory().getItems()[getSlot()].getId();
		if(item != getItem())
			return false;
		int invAmount = player.getInventory().getAmount(item);
		if(amount > invAmount) 
			amount = invAmount;
		if(amount <= 0)
			return false;
		player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), new Item(item, amount), getSlot(), false, true);

		return false;
	}
}
