package com.zyrox.saving;

import com.zyrox.model.Item;
import com.zyrox.model.container.ItemContainer;
import com.zyrox.world.entity.impl.player.Player;

public class SavePresetInventory extends SaveContainer {

	private int index;

	public SavePresetInventory(String name, int index) {
		super(name);
		this.index = index;
	}

	@Override
	public ItemContainer getContainer(Player player) {
		return player.getPresets().get(index).getInventory();
	}

	@Override
	public void loadItem(Player player, Item item, int slot) {
		if (item.getAmount() <= 0) {
			return;
		}
		
		player.getPresets().get(index).getInventory().set(slot, item);
	}

}
