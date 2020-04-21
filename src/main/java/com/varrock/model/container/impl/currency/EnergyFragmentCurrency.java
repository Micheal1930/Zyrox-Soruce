package com.varrock.model.container.impl.currency;

import com.varrock.model.Item;
import com.varrock.model.container.impl.Shop;
import com.varrock.model.definitions.ItemDefinition;

public final class EnergyFragmentCurrency extends ItemCurrency {

	public EnergyFragmentCurrency() {
		super(new Item(13653));
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
		return Shop.ENERGY_FRAGMENT_SPRITE_ID;
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
		{ "agility experience lamp", "1" },
	};

	private static final int[][] PRICE_VALUE = {
			{ 5509, 200 },
			{ 5510, 300 },
			{ 5512, 400 },
			{ 13626, 300 },
			{ 13624, 300 },
			{ 13627, 300 },
			{ 13628, 300 },

	};
}
