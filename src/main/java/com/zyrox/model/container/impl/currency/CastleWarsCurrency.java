package com.zyrox.model.container.impl.currency;

import com.zyrox.model.Item;
import com.zyrox.model.container.impl.Shop;
import com.zyrox.model.definitions.ItemDefinition;

public final class CastleWarsCurrency extends ItemCurrency {

	public CastleWarsCurrency() {
		super(new Item(4067));
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
		{ "agility experience lamp", "1" },
	};

	private static final int[][] PRICE_VALUE = {
			{ 4151, 2000 }, //abyssal whip
			{ 15486, 2000 }, //staff of light
			{ 11235, 750 }, //dark bow
			{ 14484, 50000 }, //dragon claws
			{ 19780, 15000 }, //korasi
			{ 51003, 150000 }, //elder maul
			{ 11696, 5000 }, //bandos godsword
			{ 11698, 5000 }, //saradomin godsword
			{ 11700, 5000 }, //zamorak godsword
			{ 11694, 20000 }, //armadyl godsword
			{ 49481, 15000 }, //ballista
			{ 11128, 2000 }, //berserker necklace
			{ 10887, 20000 }, //barrelchest anchor
			{ 11730, 5000 }, //saradomin sword
			{ 4716, 1500 }, //dh helm
			{ 4724, 750 }, //guthan helm
			{ 4745, 750 }, //torag helm
			{ 4753, 750 }, //verac helm
			{ 4708, 1000 }, //ahrim hood
			{ 4732, 750 }, //karil coif
			{ 22321, 15000 }, //abyssal tentacle
			{ 15241, 15000 }, //hand cannon
			{ 51902, 30000 }, //dragon crossbow
			{ 51012, 35000 }, //dragon hunter crossbow
			{ 4720, 1500 }, //dharok platebody
			{ 4728, 1000 }, //guthan platebody
			{ 4749, 1000 }, //torag platebody
			{ 4757, 1000 }, //verac platebody
			{ 4712, 1500 }, //ahrim robetop
			{ 4736, 1000 }, //karil top
			{ 6585, 750 }, //fury
			{ 20000, 25000 }, //steads
			{ 20002, 20000 }, //ragefires
			{ 20001, 25000 }, //glaivens
			{ 4722, 1500 }, //dharok legs
			{ 4730, 1000 }, //guthan legs
			{ 4751, 1000 }, //torag legs
			{ 4759, 1000 }, //verac legs
			{ 4714, 1500 }, //ahrim bottoms
			{ 4738, 1000 }, //karil skirt
			{ 15019, 1500 }, //archers ring (i)
			{ 15018, 1500 }, //seers ring (i)
			{ 15020, 1500 }, //warrior ring (i)
			{ 15220, 3000 }, //berserker ring (i)
			{ 4718, 1500 }, //dharok axe
			{ 4726, 1000 }, //guthan warspear
			{ 4747, 1000 }, //torag hammer
			{ 4755, 1000 }, //verac's flail
			{ 4710, 1000 }, //ahrim staff
			{ 4734, 1000 }, //karil's crossbow
			{ 12601, 45000 }, //ring of the gods
			{ 6914, 500 }, //master wand
			{ 12904, 35000 }, //toxic staff
			{ 20171, 35000 }, //zaryte bow
			{ 11726, 40000 }, //bandos tassets
			{ 11724, 40000 }, //bandos chestplate
			{ 49553, 70000 }, //aguish neck
			{ 49547, 70000 }, //torture neck
			{ 11732, 1000 }, //dragon boots
			{ 6920, 1000 }, //infinity boots
			{ 2577, 1000 }, //ranger boots
			{ 11718, 6000 }, //armadyl helmet
			{ 11720, 6000 }, //armadyl body
			{ 11722, 6000 }, //armadyl legs
			{ 12926, 30000 }, //blowpipe
			{ 50214, 10000 }, //team cape x
			{ 50217, 10000 }, //team cape i
			{ 42695, 50 }, //super combat pot
			{ 2581, 5000 }, //robin hood hat
			{ 11848, 6000 }, //dharok set
			{ 11850, 4000 }, //guthan set
			{ 11854, 4000 }, //torag set
			{ 11856, 4000 }, //verac set
			{ 11846, 5500 }, //ahrim set
			{ 11852, 4000 }, //karil set
			{ 4740, 25 }, //bolt racks
			{ 15332, 500 }, //overloads
	};
}
