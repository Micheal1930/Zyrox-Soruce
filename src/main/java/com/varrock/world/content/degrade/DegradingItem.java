package com.varrock.world.content.degrade;

import com.varrock.model.Item;

/**
 * Represents a degrading item
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class DegradingItem extends Item {

	/**
	 * The hits remaining
	 */
	private int hitsRemaining;

	/**
	 * The slot
	 */
	private int slot;

	/**
	 * The drop item
	 */
	private int dropItem;

	/**
	 * The next item
	 */
	private int nextItem;

	/**
	 * Whether to degrade instantly in combat
	 */
	private boolean degradeOnCombat;

	/**
	 * Represents a degradable item
	 * 
	 * @param id the id
	 * @param hitsRemaining the hits remaining
	 * @param slot the slot
	 * @param dropItem the drop item
	 * @param nextItem the next item
	 * @param degradeOnCombat whether to degrade on combat
	 */
	public DegradingItem(int id, int hitsRemaining, int slot, int dropItem, int nextItem,
			boolean degradeOnCombat) {
		super(id);
		this.setHitsRemaining(hitsRemaining);
		this.slot = slot;
		this.dropItem = dropItem;
		this.nextItem = nextItem;
		this.degradeOnCombat = degradeOnCombat;
	}

	/**
	 * Represents a degradable item
	 * 
	 * @param id the id
	 * @param degradingItem the item
	 */
	public DegradingItem(int id, DegradingItem degradingItem) {
		this(id, degradingItem.getHitsRemaining(), degradingItem.getSlot(),
				degradingItem.getDropItem(), degradingItem.getNextItem(),
				degradingItem.isDegradeOnCombat());
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
	 * Decreases the hits
	 */
	public void decreaseHits() {
		hitsRemaining--;
	}

	/**
	 * Sets the hitsRemaining
	 * 
	 * @param hitsRemaining the hitsRemaining
	 */
	public void setHitsRemaining(int hitsRemaining) {
		this.hitsRemaining = hitsRemaining;
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
		return getId() + "-" + hitsRemaining + "-" + slot + "-" + dropItem + "-" + nextItem+"-"+degradeOnCombat;
	}
	
	/**
	 * Gets degrading item from string
	 * @param s the string
	 * @return the item
	 */
	public static DegradingItem fromString(String s) {
		String[] split = s.split("-");
		int id = Integer.parseInt(split[0]);
		int hitsRemaining = Integer.parseInt(split[1]);
		int slot = Integer.parseInt(split[2]);
		int dropItem = -1;
		int nextItem = -1;
		boolean degradeOnCombat = false;
		DegradingItem item = new DegradingItem(id, hitsRemaining, slot, dropItem, nextItem, degradeOnCombat);
		return item;
	}
}
