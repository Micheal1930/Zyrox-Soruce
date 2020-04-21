package com.varrock.model.input.impl;

import com.varrock.model.Item;
import com.varrock.model.input.EnterAmount;
import com.varrock.world.entity.impl.player.Player;

public class EnterAmountToSellShop extends EnterAmount {

	private Item item;

	public EnterAmountToSellShop(Item item) {
		this.item = item;
	}
	
	@Override
	public boolean handleAmount(Player player, int amount) {
		if (player.getShop() != null) {
			if (item == null)
				return false;
			if (!player.getShop().buys(item)) {
				player.getPacketSender().sendMessage("You cannot sell this here!");
				return false;
			}

			final int contained_amount = player.getInventory().getAmount(item.getId());
			if (amount > contained_amount)
				amount = contained_amount;
			item.setAmount(amount);
			player.getInventory().switchItemShops(player.getShop(), item, player.getInventory().getSlot(item.getId()), false, true);
		}

		return false;
	}

}
