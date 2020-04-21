package com.varrock.saving;

import com.varrock.model.Item;
import com.varrock.model.container.ItemContainer;
import com.varrock.world.entity.impl.player.Player;

public class SavePresetEquipment extends SaveContainer {

	private int index;

	public SavePresetEquipment(String name, int index) {
		super(name);
		this.index = index;
	}
	
	@Override
	public ItemContainer getContainer(Player player) {
		return player.getPresets().get(index).getEquipment();
	}

	@Override
	public void loadItem(Player player, Item item, int slot) {
		if (item.getAmount() <= 0) {
			return;
		}
		
		player.getPresets().get(index).getEquipment().set(slot, item);
	}

}
