package com.varrock.model.container.impl.currency;
import com.varrock.model.Item;
import com.varrock.model.container.impl.Shop;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.util.Misc;
import com.varrock.world.entity.impl.player.Player;

/**
 * An implementation of {@link org.niobe.model.container.impl.Shop.Currency}
 * that represents an {@link org.niobe.model.GameCharacter#getPlayerFields()#getBossKillPoints()}
 * currency for shops.
 *
 * @author relex lawl
 */
public final class BossPointCurrency implements Shop.Currency {

	@Override
	public String getName() {
		return "Boss points";
	}

	@Override
	public int getAmount(Player player) {
		return player.getBossPoints();
	}

	@Override
	public void delete(Player player, int amount) {
		if (amount > 0) {
			int delete = getAmount(player) - amount;
			player.setBossPoints(delete);
			player.getPacketSender().sendMessage("You now have " + Misc.insertCommasToNumber(delete) + " " + getName() + ".");
		}
	}

	@Override
	public void add(Player player, int amount) {
		if (amount > 0) {
			int add = getAmount(player) + amount;
			player.setBossPoints(add);
			player.getPacketSender().sendMessage("You now have " +  Misc.insertCommasToNumber(add) + " " + getName() + ".");
		}
	}

	@Override
	public int getSellPrice(Item item) {
		return getPointPrice(item);
	}
	
	@Override
	public int getBuyPrice(Item item) {
		return 0;
	}
	
	@Override
	public int getSpriteId() {
		return Shop.TOKEN_SPRITE_ID;
	}
	
	/**
	 * Gets the point price for said {@link item}.
	 * @param item	The {@link org.niobe.model.Item} to get special price for.
	 * @return		The points needed in order to purchase said item.
	 */
	private static int getPointPrice(Item item) {
		final String name = ItemDefinition.forId(item.getId()).getName().toLowerCase();
		for (String[] value : POINT_VALUE) {
			if (name.contains(value[0])) {
				return Integer.valueOf(value[1]);
			}
		}
		return 2147000000;
	}
	
	private static final String[][] POINT_VALUE = {
		
		{ "ahrim", "200" },
		{ "dharok", "200" },
		{ "guthan", "200" },
		{ "torag", "200" },
		{ "verac", "200" },
		{ "crystal key", "20" },
		{ "half of a key", "10" },
		{ "berserker ring (i)", "300" },
		{ "seers' ring (i)", "100" },
		{ "warrior ring (i)", "100" },
		{ "archers' ring (i)", "100" },
		{ "zamorakian spear", "80" },
		{ "ring of wealth", "100" },
		{ "staff of light", "50" },
		{ "hand cannon", "100" },
		{ "dragon scimitar", "100" },
		{ "abyssal whip", "50" },
		{ "saradomin sword", "100" },
		{ "fighter hat", "60" },
		{ "fighter torso", "100" },
		{ "fire cape", "50" },
		{ "tokhaar", "150" },
		{ "armadyl crossbow", "500" },
		{ "zamorakian hasta", "250" },
		{ "dragon defender", "50" },
		{ "mystery box", "30" },
	};
}
