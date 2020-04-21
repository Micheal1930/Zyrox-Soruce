package com.varrock.saving;

import com.varrock.model.Item;
import com.varrock.model.container.ItemContainer;
import com.varrock.world.entity.impl.player.Player;

public class SaveEquipment extends SaveContainer {

	public SaveEquipment(String name) {
		super(name);
	}
	
	@Override
	public ItemContainer getContainer(Player player) {
		return player.getEquipment();
	}

	@Override
	public void loadItem(Player player, Item item, int slot) {
		if (item.getAmount() <= 0) {
			return;
		}
		
		player.getEquipment().set(slot, item);
	}

}
