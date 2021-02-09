package com.zyrox.saving;

import com.zyrox.model.Item;
import com.zyrox.model.container.ItemContainer;
import com.zyrox.world.entity.impl.player.Player;

public class SaveRunePouch extends SaveContainer {

	public SaveRunePouch(String name) {
		super(name);
	}

	@Override
	public ItemContainer getContainer(Player player) {
		return player.getRunePouch();
	}

	@Override
	public void loadItem(Player player, Item item, int slot) {
		if (slot >= 0) {
			player.getRunePouch().set(slot, item);
		} else {
			player.getRunePouch().add(item, "save rune pouch");
		}
	}

}