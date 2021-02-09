package com.zyrox.world.content.degrade;

import com.zyrox.model.Item;

/**
 * Represents a degrading item
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class DegradingDataItem extends Item {

	/**
	 * The hits remaining
	 */
	private final int hitsRemaining;

	/**
	 * The slot
	 */
	private final int slot;

	/**
	 * The drop item
	 */
	private final int dropItem;

	/**
	 * The next item
	 */
	private final int nextItem;

	/**
	 * Whether to degrade instantly in combat
	 */
	private final boolean degradeOnCombat;

	/**
	 * Represents a degradable item
	 * 
	 * @param id              the id
	 * @param hitsRemaining   the hits remaining
	 * @param slot            the slot
	 * @param dropItem        the drop item
	 * @param nextItem        the next item
	 * @param degradeOnCombat whether to degrade on combat
	 */
	public DegradingDataItem(int id, int hitsRemaining, int slot, int dropItem, int nextItem, boolean degradeOnCombat) {
		super(id);
		this.hitsRemaining = hitsRemaining;
		this.slot = slot;
		this.dropItem = dropItem;
		this.nextItem = nextItem;
		this.degradeOnCombat = degradeOnCombat;
	}

	/**
	 * Represents a degradable item
	 * 
	 * @param id            the id
	 * @param degradingItem the item
	 */
	public DegradingDataItem(int id, DegradingDataItem degradingItem) {
		this(id, degradingItem.getHitsRemaining(), degradingItem.getSlot(), degradingItem.getDropItem(),
				degradingItem.getNextItem(), degradingItem.isDegradeOnCombat());
	}

	/**
	 * Gets the hitsRemaining
	 *
	 * @return the hitsRemaining
	 */
	public int getHitsRemaining() {
		return hitsRemaining;
	}

	/**
	 * Gets the slot
	 *
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * Gets the dropItem
	 *
	 * @return the dropItem
	 */
	public int getDropItem() {
		return dropItem;
	}

	/**
	 * Gets the nextItem
	 *
	 * @return the nextItem
	 */
	public int getNextItem() {
		return nextItem;
	}

	/**
	 * Gets the degradeOnCombat
	 *
	 * @return the degradeOnCombat
	 */
	public boolean isDegradeOnCombat() {
		return degradeOnCombat;
	}

	@Override
	public String toString() {
		return getId() + "-" + hitsRemaining + "-" + slot + "-" + dropItem + "-" + nextItem;
	}

	public DegradingItem toItem() {
		return new DegradingItem(getId(), hitsRemaining, slot, dropItem, nextItem, degradeOnCombat);
	}
}
