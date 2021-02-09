package com.zyrox.model.container.impl;

import java.util.List;
import java.util.Optional;

import com.zyrox.model.Item;
import com.zyrox.model.container.ItemContainer;
import com.zyrox.model.container.StackType;
import com.zyrox.model.container.impl.Bank.BankSearchAttributes;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.model.log.impl.ShopSellLog;
import com.zyrox.util.Misc;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Represents a player's inventory item container.
 * 
 * @author relex lawl
 */

public class Inventory extends ItemContainer {

	/**
	 * The Inventory constructor.
	 * @param player	The player who's inventory is being represented.
	 */
	public Inventory(Player player) {
		super(player);
	}

	@Override
	public Inventory switchItemShops(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		if (slot < 0 ||
				getItems()[slot].getId() != item.getId()
				|| item.getAmount() <= 0) {
			return this;
		}


		Shop shop = null;
		int price = 0;
		if (to instanceof Shop) {
			shop = (Shop) to;

			if (shop.getId() == 22 && shop.isFull()) {
				shop.resetItems();
			}
		}
		if (!to.hasEmptySlot()) {
			if (to.stackType() == StackType.STACKS && !to.contains(item.getId())
					|| to.stackType() != StackType.STACKS && !item.getDefinition().isStackable()) {
				to.full();
				return this;
			}
		}
		final ItemDefinition definition = ItemDefinition.forId(item.getId());
		if (!item.tradeable(getPlayer())) {
			getPlayer().sendMessage("That item is untradeable.");
			return this;
		}

		if (!definition.isStackable() && to.stackType() != StackType.STACKS) {
			if (item.getAmount() > to.getFreeSlots()) {
				item.setAmount(to.getFreeSlots());
				if (item.getAmount() <= 0) {
					to.full();
					return this;
				}
			}
		}
		int maxAmount = getAmount(item.getId());
		if (item.getAmount() > maxAmount)
			item.setAmount(maxAmount);
		if (!to.canAccept(this, item, slot))
			return this;
		if (shop != null) {

			final int buy_price = shop.getCurrency().getBuyPrice(item);
			price = buy_price * item.getAmount();

			if (price <= 0) {
				getPlayer().sendMessage("You cannot sell this here!");
				return this;
			}

			if (shop.getId() == 27 && price >= 10_000_000) {
				getPlayer().sendMessage("You cannot sell items worth over 10,000,000 gp in total!");
				return this;
			}

			new ShopSellLog(getPlayer().getName(),
					item.getDefinition().getName(),
					item.getId(), item.getAmount(),
					price, shop.getCurrency().getName(),
					Misc.getTime()).submit();
		}

		delete(item, slot, false, to);
		if (to instanceof Bank && ItemDefinition.forId(item.getId()).getUnnotedId() > 0)
			item.setId(ItemDefinition.forId(item.getId()).getUnnotedId());
		to.add(item, false);
		if (shop != null)
			shop.getCurrency().add(getPlayer(), price);
		if (sort && getAmount(item.getId()) <= 0)
			sortItems();
		if (refresh) {
			refreshItems();
			to.refreshItems();
		}

		return this;
	}

	@Override
	public Inventory switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		if (getItems()[slot].getId() != item.getId())
			return this;
		if (to.getFreeSlots() <= 0 && !(to.contains(item.getId()) && item.getDefinition().isStackable())) {
			to.full();
			return this;
		}
		if(to instanceof BeastOfBurden || to instanceof PriceChecker) {
			if(to instanceof PriceChecker && !item.sellable()) {
				getPlayer().getPacketSender().sendMessage("You cannot do that with this item because it cannot be sold.");
				return this;
			}
			if(item.getAmount() > to.getFreeSlots()) {
				if(!item.getDefinition().isStackable()) {
					item.setAmount(to.getFreeSlots());
				}
			}
		}
		if(to instanceof Bank) {
			int checkId = ItemDefinition.forId(item.getId()).isNoted() ? item.getDefinition().getUnnotedId() : item.getId();
			if(to.getAmount(checkId) + item.getAmount() >= Integer.MAX_VALUE || to.getAmount(checkId) + item.getAmount() <= 0) {
				int canBank = (Integer.MAX_VALUE - to.getAmount(checkId)) ;
				if(canBank == 0) {
					getPlayer().getPacketSender().sendMessage("You cannot deposit more of that item into your bank.");
					return this;
				}
				item.setAmount(canBank);
			}
		}

		Shop shop = null;
		int price = 0;
		if (to instanceof Shop) {
			shop = (Shop) to;

/*			if (shop.getId() == 22 && shop.isFull()) {
				shop.resetItems();
			}*/
		}

		if (shop != null) {

			final int buy_price = shop.getCurrency().getBuyPrice(item);
			price = buy_price * item.getAmount();

			if (price <= 0) {
				getPlayer().sendMessage("You cannot sell this here!");
				return this;
			}

			if (price >= 10_000_000) {
				getPlayer().sendMessage("You cannot sell items worth over 10,000,000 gp in total!");
				return this;
			}

			new ShopSellLog(getPlayer().getName(),
					item.getDefinition().getName(),
					item.getId(), item.getAmount(),
					price, shop.getCurrency().getName(),
					Misc.getTime()).submit();

		}

		delete(item, slot, refresh, to);
		if (to instanceof Bank && ItemDefinition.forId(item.getId()).isNoted() && !ItemDefinition.forId(item.getDefinition().getUnnotedId()).isNoted())
			item.setId(item.getDefinition().getUnnotedId());

		to.add(item, "inventory switch item");

		if (shop != null)
			shop.getCurrency().add(getPlayer(), price);

		if (sort && getAmount(item.getId()) <= 0)
			sortItems();
		if (refresh) {
			refreshItems();
			to.refreshItems();
		}
		if(to instanceof Bank && getPlayer().getBankSearchingAttribtues().isSearchingBank() && getPlayer().getBankSearchingAttribtues().getSearchedBank() != null) {
			BankSearchAttributes.addItemToBankSearch(getPlayer(), item);
		}
		return this;
	}

	@Override
	public int capacity() {
		return 28;
	}

	@Override
	public StackType stackType() {
		return StackType.DEFAULT;
	}

	@Override
	public Inventory refreshItems() {
		if (getPlayer().getBank().isOpen()) {
			getPlayer().getPacketSender().sendItemContainer(getPlayer().getInventory(), Bank.INVENTORY_INTERFACE_ID);
		}
		
		getPlayer().getPacketSender().sendItemContainer(this, INTERFACE_ID);
		return this;
	}

	@Override
	public Inventory full() {
		getPlayer().getPacketSender().sendMessage("Not enough space in your inventory.");
		return this;
	}

	/**
	 * Adds a set of items into the inventory.
	 *
	 * @param item
	 * the set of items to add.
	 */
	public void addItemSet(Item[] item) {
		for (Item addItem : item) {
			if (addItem == null) {
				continue;
			}
			add(addItem, "Array of items add to inventory");
		}
	}
	
	public void addItemSet(List<Item> item) {
		for (Item addItem : item) {
			if (addItem == null) {
				continue;
			}
			add(addItem, "Array of items add to inventory");
		}
	}

	/**
	 * Deletes a set of items from the inventory.
	 *
	 * @param optional
	 * the set of items to delete.
	 */
	public void deleteItemSet(Optional<Item[]> optional) {
		if(optional.isPresent()) {
			for (Item deleteItem : optional.get()) {
				if (deleteItem == null) {
					continue;
				}

				delete(deleteItem);
			}
		}
	}

	public static final int INTERFACE_ID = 3214;
}
