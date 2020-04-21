package com.varrock.world.content;

import com.varrock.model.container.ItemContainer;
import com.varrock.model.container.StackType;
import com.varrock.world.entity.impl.player.Player;

/**
 * A class that handles the usage of a Rune pouch.
 * 
 * @author Blake
 *
 */
public class RunePouch extends ItemContainer {
	
	/**
	 * The interface id.
	 */
	public static final int INTERFACE_ID = 36100;
	
	/**
	 * The rune container id.
	 */
	public static final int RUNE_CONTAINER_ID = 36110;
	
	/**
	 * The inventory container id.
	 */
	public static final int INVENTORY_CONTAINER_ID = 36111;

	/**
	 * Constructs a new {@link RunePouch}.
	 * 
	 * @param player
	 *            the player
	 */
	public RunePouch(Player player) {
		super(player, 3, 16_000);
	}

	@Override
	public int capacity() {
		return 3;
	}

	@Override
	public StackType stackType() {
		return StackType.DEFAULT;
	}

	@Override
	public ItemContainer refreshItems() {
		getPlayer().getPacketSender().sendItemContainer(getItems(), RUNE_CONTAINER_ID);
		getPlayer().getPacketSender().sendItemContainer(getPlayer().getInventory().getItems(), INVENTORY_CONTAINER_ID);
		return this;
	}

	@Override
	public ItemContainer full() {
		getPlayer().getPacketSender().sendMessage("Not enough space in your rune pouch.");
		return this;
	}
	
	/**
	 * Opens the interface.
	 */
	public void open() {
		refreshItems();
		
		getPlayer().getPacketSender().sendInterface(INTERFACE_ID);
	}
	
	/**
	 * Checks if the specified id is a rune.
	 * 
	 * @param itemId
	 *            The id.
	 * @return <code>true</code> if the id is a rune
	 */
	public static boolean isRune(int itemId) {
		if (itemId >= 554 && itemId <= 566) {
			return true;
		}
		
		if (itemId >= 4694 && itemId <= 4699) {
			return true;
		}
		
		if (itemId == 9075) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Deposits the specified item into the pouch.
	 * 
	 * @param itemId
	 *            The item id.
	 * @param slot
	 *            The slot.
	 * @param amount
	 *            The amount.
	 */
	public void deposit(int itemId, int slot, int amount) {
		if (!getPlayer().getInventory().slotContainsItem(slot, itemId)) {
			return;
		}

		if (!isRune(itemId)) {
			getPlayer().getPacketSender().sendMessage("You can only deposit runes into your rune pouch.");
			return;
		}
		
		if (amount > getPlayer().getInventory().getAmountForSlot(slot)) {
			amount = getPlayer().getInventory().getAmountForSlot(slot);
		}
		
		if ((long) amount + (long) getAmount(itemId) > getMaxSlotAmount()) {
			amount = getMaxSlotAmount() - getAmount(itemId);
		}

		if(getFreeSlots() == 0 && !contains(itemId)) {
			getPlayer().getPacketSender().sendMessage("You can only store 3 types of runes in your rune pouch.");
			return;
		}

		if (amount == 0) {
			full();
			return;
		}
		
		add(itemId, amount);
		
		getPlayer().getInventory().delete(itemId, amount);

		refreshItems();
	}

	/**
	 * Withdraws an item from the Rune pouch.
	 * 
	 * @param itemId
	 *            The item id.
	 * @param slot
	 *            The slot.
	 * @param amount
	 *            The amount.
	 */
	public void withdraw(int itemId, int slot, int amount) {
		if (!slotContainsItem(slot, itemId)) {
			return;
		}
		
		if (amount > getAmountForSlot(slot)) {
			amount = getAmountForSlot(slot);
		}
		
		if ((long) amount + (long) getPlayer().getInventory().getAmount(itemId) > getPlayer().getInventory().getMaxSlotAmount()) {
			amount = getPlayer().getInventory().getMaxSlotAmount() - getPlayer().getInventory().getAmount(itemId);
		}
		
		if (amount == 0) {
			getPlayer().getInventory().full();
			return;
		}
		
		getPlayer().getInventory().add(itemId, amount);
		
		delete(itemId, amount);
		
		refreshItems();
	}

}
