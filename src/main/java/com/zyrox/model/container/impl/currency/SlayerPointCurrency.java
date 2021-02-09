package com.zyrox.model.container.impl.currency;
import com.zyrox.model.Item;
import com.zyrox.model.container.impl.Shop;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.util.Misc;
import com.zyrox.world.entity.impl.player.Player;

/**
 * An implementation of {@link org.niobe.model.container.impl.Shop.Currency}
 * that represents an {@link org.niobe.model.GameCharacter#getPlayerFields()#getBossKillPoints()}
 * currency for shops.
 *
 * @author relex lawl
 */
public final class SlayerPointCurrency implements Shop.Currency {

	@Override
	public String getName() {
		return "Slayer points";
	}

	@Override
	public int getAmount(Player player) {
		return player.getPointsHandler().getSlayerPoints();
	}

	@Override
	public void delete(Player player, int amount) {
		if (amount > 0) {
			int delete = getAmount(player) - amount;
			player.getPointsHandler().setSlayerPoints(delete, false);
			player.getPacketSender().sendMessage("You now have " + Misc.insertCommasToNumber(delete) + " " + getName() + ".");
		}
	}

	@Override
	public void add(Player player, int amount) {
		if (amount > 0) {
			int add = getAmount(player) + amount;
			player.getPointsHandler().setSlayerPoints(add, false);
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
		return Shop.SLAYER_POINT_SPRITE;
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
		
		{ "slayer helmet", "350" },
		{ "ring of slaying", "50" },
		{ "abyssal whip", "100" },
		{ "staff of light", "100" },
		{ "fighter torso", "250" },
		{ "ring of wealth", "250" },
		{ "bonecrusher", "150" },
		{ "dragon boots", "50" },
		{ "amulet of fury", "100" },
		{ "ranger boots", "100" },
		{ "robin hood hat", "85" },
		{ "zamorakian spear", "150" },
		{ "berserker ring (i)", "300" },
		{ "seers' ring (i)", "200" },
		{ "archers' ring (i)", "200" },
		{ "warrior ring (i)", "200" },
		{ "balmung", "250" },
		{ "saradomin sword", "100" },
		{ "barrelchest anchor", "100" },
		{ "dark bow", "100" },
		{ "hand cannon shot", "1" },
		{ "hand cannon", "150" },
	};
}
