package com.zyrox.model.container.impl.currency;

import com.zyrox.model.Item;
import com.zyrox.model.container.impl.Shop;
import com.zyrox.model.definitions.ItemDefinition;

public final class AgilityTicketCurrency extends ItemCurrency {

	public AgilityTicketCurrency() {
		super(new Item(2996));
	}
	
	@Override
	public int getSellPrice(Item item) {
		return getPrice(item);
	}

	@Override
	public int getBuyPrice(Item item) {
		return 0;
	}

	@Override
	public int getSpriteId() {
		return Shop.TICKET_SPRITE_ID;
	}
	
	private static int getPrice(Item item) {
		final String name = ItemDefinition.forId(item.getId()).getName().toLowerCase();
		for (String[] value : POINT_VALUE) {
			if (name.contains(value[0])) {
				return Integer.valueOf(value[1]);
			}
		}
		for (int[] value : PRICE_VALUE) {
			if (item.getId() == value[0]) {
				return Integer.valueOf(value[1]);
			}
		}
		return 200000;
	}
	
	private static final String[][] POINT_VALUE = {
		{ "agile", "100" },
	};

	private static final int[][] PRICE_VALUE = {

	};
}
