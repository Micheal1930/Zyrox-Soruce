package com.varrock.model.container.impl.currency;

import com.varrock.model.Item;
import com.varrock.model.container.impl.Shop;
import com.varrock.model.container.impl.Shop.Currency;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.world.entity.impl.player.Player;

/**
 * An implementation of {@link org.niobe.model.container.impl.Shop.Currency}
 * that represents an {@link org.niobe.model.Item} currency for shops.
 *
 * @author relex lawl
 */
public class ItemCurrency implements Currency {
	
	/**
	 * The ItemCurrency constructor.
	 * @param item	The {@link org.niobe.model.Item} being used as the currency.
	 */
	public ItemCurrency(Item item) {
		this.item = item;
	}
	
	/**
	 * The {@link org.niobe.model.Item} being used as the currency.
	 */
	private final Item item;

	@Override
	public String getName() {
		return item.getDefinition().getName().toLowerCase();
	}

	@Override
	public int getAmount(Player player) {
		return player.getInventory().getAmount(item.getId());
	}

	@Override
	public void delete(Player player, int amount) {
		if (amount <= 0)
			return;
		if (item.getId() == 995 && !player.getInventory().contains(new Item(995, amount))) {
			player.setMoneyInPouch(player.getMoneyInPouch() - amount);
			player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
		} else {
			player.getInventory().delete(new Item(item.getId(), amount));
		}
	}

	@Override
	public void add(Player player, int amount) {
		if (amount <= 0)
			return;
		Item currency = new Item(item.getId(), amount);
		if(this instanceof CoinCurrency) {
			if(player.getInventory().getFreeSlots() == 0) {
				player.addToPouch(currency.getAmount());
				return;
			}
		}
		player.getInventory().add(currency);
	}
	
	@Override
	public int getSellPrice(Item item) {
		return ItemDefinition.forId(item.getId()).getValue();
	}

	@Override
	public int getBuyPrice(Item item) {
		final int value = ItemDefinition.forId(item.getId()).getValue();
		if(item.getId() == 299 || item.getId() == 15484) {
			return 1;
		}
		return (int) (value * .75);
	}

	@Override
	public int getSpriteId() {
		return Shop.COIN_SPRITE_ID;
	}
}
