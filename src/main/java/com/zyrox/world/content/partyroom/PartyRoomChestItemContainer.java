package com.zyrox.world.content.partyroom;

import com.zyrox.model.Item;
import com.zyrox.model.container.ItemContainer;
import com.zyrox.model.container.StackType;
import com.zyrox.util.Misc;

/**
 * Represents the Party Room chest item container
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class PartyRoomChestItemContainer extends ItemContainer {

	/**
	 * The drop party chest size
	 */
	private static final int MAIN_CONTAINER_SIZE = 216;

	@Override
	public int capacity() {
		return MAIN_CONTAINER_SIZE;
	}

	@Override
	public StackType stackType() {
		return StackType.DEFAULT;
	}

	@Override
	public ItemContainer refreshItems() {
		return null;
	}

	@Override
	public ItemContainer full() {
		return null;
	}

	public Item getRandom() {
		Item[] item = getValidItemsArray();
		Item reward = item[Misc.random(item.length - 1)];
		int amount = 1;
		if (reward.getDefinition().isStackable()) {
			if (reward.getAmount() > 1) {
				amount = Misc.exclusiveRandom(1, reward.getAmount());
			}
		}
		return new Item(reward.getId(), amount);
	}
}
