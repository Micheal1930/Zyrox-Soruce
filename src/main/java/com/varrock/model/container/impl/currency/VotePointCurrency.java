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
public final class VotePointCurrency implements Shop.Currency {

	@Override
	public String getName() {
		return "Vote points";
	}

	@Override
	public int getAmount(Player player) {
		return player.getPointsHandler().getVotingPoints();
	}

	@Override
	public void delete(Player player, int amount) {
		if (amount > 0) {
			int delete = getAmount(player) - amount;
			player.getPointsHandler().setVotingPoints(delete, false);
			player.getPacketSender().sendMessage("You now have " + Misc.insertCommasToNumber(delete) + " " + getName() + ".");
		}
	}

	@Override
	public void add(Player player, int amount) {
		if (amount > 0) {
			int add = getAmount(player) + amount;
			player.getPointsHandler().setVotingPoints(add, false);
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
		return Shop.VOTING_SPRITE_ID;
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
		
		{ "overload", "1" },
		{ "super combat", "1" },
		{ "emerald rapier", "5" },
		{ "fire cape", "4" },
		{ "tokhaar", "8" },
		{ "eye of the", "250" },
		{ "mystery box", "4" },
		{ "charm box", "4" },
		{ "gnome scarf", "1" },
		{ "fishbowl", "1" },
		{ "diving", "1" },
		{ "top hat", "1" },
		{ "flipper", "1" },
		{ "greegree", "25" },
		{ "halo", "75" },
		{ "infinity", "300" },
		{ "voting hat", "100" },
		{ "fury", "4" },
		{ "dragon boots", "4" },
		{ "staff of light", "25" },
		{ "spectral", "100" },
	};
}
