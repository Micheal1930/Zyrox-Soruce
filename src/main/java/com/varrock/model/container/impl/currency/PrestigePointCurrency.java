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
public final class PrestigePointCurrency implements Shop.Currency {

	@Override
	public String getName() {
		return "Prestige points";
	}

	@Override
	public int getAmount(Player player) {
		return player.getPointsHandler().getPrestigePoints();
	}

	@Override
	public void delete(Player player, int amount) {
		if (amount > 0) {
			int delete = getAmount(player) - amount;
			player.getPointsHandler().setPrestigePoints(delete, false);
			player.getPacketSender().sendMessage("You now have " + Misc.insertCommasToNumber(delete) + " " + getName() + ".");
		}
	}

	@Override
	public void add(Player player, int amount) {
		if (amount > 0) {
			int add = getAmount(player) + amount;
			player.getPointsHandler().setPrestigePoints(add, false);
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
		return Shop.PRESTIGE_POINT_SPRITE;
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
		
		{ "magic shortbow (i)", "3000" },

	};
}
