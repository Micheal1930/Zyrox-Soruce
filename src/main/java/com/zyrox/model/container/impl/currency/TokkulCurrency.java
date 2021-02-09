package com.zyrox.model.container.impl.currency;

import com.zyrox.model.Item;
import com.zyrox.model.container.impl.Shop;
import com.zyrox.model.definitions.ItemDefinition;

public final class TokkulCurrency extends ItemCurrency {

	public TokkulCurrency() {
		super(new Item(6529));
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
		return Shop.TOKKUL_SPRITE;
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
		{ "pet tztok", "300000" },
		{ "copper ore", "10" },
		{ "tin ore", "10" },
		{ "iron ore", "25" },
		{ "coal", "30" },
		{ "silver ore", "30" },
		{ "gold ore", "40" },
		{ "mithril ore", "70" },
		{ "adamantite ore", "120" },
		{ "runite ore", "250" },
		{ "uncut sapphire", "20" },
		{ "uncut emerald", "40" },
		{ "uncut ruby", "70" },
		{ "uncut ruby", "70" },
		{ "uncut diamond", "150" },
		{ "uncut dragonstone", "1600" },
		{ "berserker necklace", "22000" },
		{ "uncut onyx", "50000" },
		{ "toktz-ket-xil", "5000" },
		{ "toktz-xil-ak", "5000" },
		{ "toktz-mej-tal", "5000" },
		{ "toktz-xil-ul", "20" },
		{ "tzhaar-ket-om", "800" },
		{ "obsidian cape", "800" },

	};

	private static final int[][] PRICE_VALUE = {

	};
}
