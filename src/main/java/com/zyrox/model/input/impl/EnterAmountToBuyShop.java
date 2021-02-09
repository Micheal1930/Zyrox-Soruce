package com.zyrox.model.input.impl;

import com.zyrox.model.Item;
import com.zyrox.model.container.impl.Shop;
import com.zyrox.model.input.EnterAmount;
import com.zyrox.world.entity.impl.player.Player;

public class EnterAmountToBuyShop extends EnterAmount {

	private int slotX;

	public EnterAmountToBuyShop(int slotX) {
		this.slotX = slotX;
	}
	
	@Override
	public boolean handleAmount(Player player, int amount) {
		Shop shop = player.getShop();
		if (shop != null) {

			if(amount > 500 && shop.getItems()[slotX].getDefinition().isStackable()) {
				player.sendMessage("You can only buy 500 at a time of stackable items.");
				amount = 500;
			}

			Item item = shop.getItems()[slotX].copy().setAmount(amount);

			shop.switchItemShops(player.getInventory(), item, slotX, false, true);

			player.lastBuyX = amount;
			player.getPA().setInterfaceActions(20703, "Value", "Buy 1", "Buy 5", "Buy 10", "Buy "+amount+"", "Buy X");

		}

		return false;
	}

}
