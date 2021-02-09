package com.zyrox.model.container.impl.currency;

import com.zyrox.model.Item;
import com.zyrox.model.container.impl.Shop;
import com.zyrox.model.definitions.ItemDefinition;

public final class StardustCurrency extends ItemCurrency {

	public StardustCurrency() {
		super(new Item(13727));
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
		return Shop.STARDUST_SPRITE_ID;
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
		{ "dragon pic", "500" },
		{ "adze", "1500" },
		{ "3rd age", "2000" },
		{ "charm box", "50" },
	};

	private static final int[][] PRICE_VALUE = {

	};
}
