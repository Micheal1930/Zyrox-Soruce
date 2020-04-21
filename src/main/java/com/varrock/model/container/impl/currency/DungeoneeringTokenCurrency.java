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
public final class DungeoneeringTokenCurrency implements Shop.Currency {

	@Override
	public String getName() {
		return "Dungeoneering tokens";
	}

	@Override
	public int getAmount(Player player) {
		return player.getPointsHandler().getDungeoneeringTokens();
	}

	@Override
	public void delete(Player player, int amount) {
		if (amount > 0) {
			int delete = getAmount(player) - amount;
			player.getPointsHandler().setDungeoneeringTokens(delete, false);
			player.getPacketSender().sendMessage("You now have " + Misc.insertCommasToNumber(delete) + " " + getName() + ".");
		}
	}

	@Override
	public void add(Player player, int amount) {
		if (amount > 0) {
			int add = getAmount(player) + amount;
			player.getPointsHandler().setDungeoneeringTokens(add, false);
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
		return Shop.DUNGEONEERING_TOKEN_SPRITE;
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
		
		{ "chaotic longsword", "200000" },
		{ "chaotic rapier", "200000" },
		{ "chaotic maul", "200000" },
		{ "chaotic crossbow", "200000" },
		{ "chaotic staff", "200000" },
		{ "chaotic kiteshield", "200000" },
		{ "eagle-eye", "200000" },
		{ "farseer", "200000" },
		{ "arcane stream", "75000" },
		{ "charming imp", "75000" },
		{ "tome of frost", "75000" },
		{ "bonecrusher", "50000" },
		{ "charming imp", "75000" },
		{ "ring of vigour", "150000" },
	};
}
