package com.varrock.model.container.impl.currency;

import com.varrock.model.Item;
import com.varrock.model.container.impl.Shop;
import com.varrock.world.entity.impl.player.Player;

/**
 * An implementation of {@link org.niobe.model.container.impl.Shop.Currency} used
 * for free shop items, such as the starting area shop for non-skillers.
 *
 * @author relex lawl
 */
public final class FreeCurrency implements Shop.Currency {

	@Override
	public String getName() {
		return "coins";
	}

	@Override
	public int getAmount(Player player) {
		return 0;
	}

	@Override
	public void delete(Player player, int amount) {
		
	}

	@Override
	public void add(Player player, int amount) {
		
	}

	@Override
	public int getSellPrice(Item item) {
		return 0;
	}

	@Override
	public int getBuyPrice(Item item) {
		return 0;
	}

	@Override
	public int getSpriteId() {
		return Shop.COIN_SPRITE_ID;
	}
}
