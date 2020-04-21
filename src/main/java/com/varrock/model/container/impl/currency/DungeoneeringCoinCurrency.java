package com.varrock.model.container.impl.currency;

import com.varrock.model.Item;
import com.varrock.model.container.impl.Shop;
import com.varrock.model.definitions.ItemDefinition;

public final class DungeoneeringCoinCurrency extends ItemCurrency {

	public DungeoneeringCoinCurrency() {
		super(new Item(18201));
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
		return Shop.COIN_SPRITE_ID;
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
		{ "bronze hatchet", "5" },
		{ "logs", "455" },
		{ "tinderbox", "140" },
		{ "raw heim crab", "2800" },
		{ "raw red-eye", "3300" },
		{ "raw dusk eel", "4500" },
		{ "raw giant flatfish", "4500" },
		{ "raw short-finned eel", "5400" },
		{ "raw web snipper", "7000" },
		{ "raw bouldabass", "8600" },
		{ "raw salve eel", "9200" },
		{ "raw blue crab", "10000" },
		{ "super strength (3)", "2800" },
		{ "super attack (3)", "2000" },
		{ "super defence (3)", "2600" },
		{ "ranging potion (3)", "5400" },
		{ "magic potion (3)", "8300" },
		{ "prayer potion (3)", "8500" },
		{ "adamant scimitar", "1500" },
		{ "yew shortbow", "7200" },
		{ "adamant arrow", "119" },
		{ "staff of water", "1000" },
		{ "air rune", "120" },
		{ "mind rune", "120" },
	};

	private static final int[][] PRICE_VALUE = {

	};
}
