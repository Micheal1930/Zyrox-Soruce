package com.zyrox.saving;

import com.zyrox.model.Item;
import com.zyrox.model.container.ItemContainer;
import com.zyrox.world.entity.impl.player.Player;

public class SaveInventory extends SaveContainer {

	public SaveInventory(String name) {
		super(name);
	}

	@Override
	public ItemContainer getContainer(Player player) {
		return player.getInventory();
	}

	@Override
	public void loadItem(Player player, Item item, int slot) {
		if (item.getAmount() <= 0) {
			return;
		}
		
		if(slot >= 0) {
			player.getInventory().set(slot, item);
		} else {
			player.getInventory().add(item, "save inventory");
		}
	}

}