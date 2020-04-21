package com.varrock.model;

import com.varrock.GameSettings;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.world.content.Effigies;
import com.varrock.world.entity.impl.player.Player;


/**
 * Represents an item which is owned by a player.
 * 
 * @author relex lawl
 */

public class Item {

	/**
	 * An Item object constructor.
	 * @param id		Item id.
	 * @param amount	Item amount.
	 */
	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	/**
	 * An Item object constructor.
	 * @param id		Item id.
	 */
	public Item(int id) {
		this(id, 1);
	}

	/**
	 * Returns an {@code Item} instance of the type {@code id}, and a stack size of {@code 1}.
	 *
	 * @throws IllegalArgumentException if {@code id} is invalid.
	 */
	public static Item of(int id) {
		if (ItemDefinition.orNull(id) == null) {
			throw new IllegalArgumentException("undefined item: " + id);
		}
		return new Item(id, 1);
	}

	/**
	 * Returns an {@code Item} instance of the type {@code id}, and a stack size of {@code amount}.
	 *
	 * @throws IllegalArgumentException if {@code id} is invalid, or {@code amount} is {@code < 1}.
	 */
	public static Item of(int id, int amount) {
		if (ItemDefinition.orNull(id) == null) {
			throw new IllegalArgumentException("undefined item: " + id);
		}
		if (amount < 1) {
			throw new IllegalArgumentException("amount zero or negative: " + amount);
		}
		return new Item(id, amount);
	}
	
	public int getNormalId() {
		ItemDefinition def = ItemDefinition.forId(this.getId());
		if(def != null) {
			if(def.isNoted()) {
				return def.getUnnotedId();
			} else {
				return this.getId();
			}
		}
		return this.getId();
	}
	
	
	/**
	 * The item id.
	 */
	protected int id;

	/**
	 * Gets the item's id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the item's id.
	 * @param id	New item id.
	 */
	public Item setId(int id) {
		this.id = id;
		return this;
	}

	/**
	 * Amount of the item.
	 */
	private int amount;

	/**
	 * Gets the amount of the item.
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * Same as getAmount(), gets the amount of the item.
	 * @return
	 */
	public int getCount() {
		return getAmount();
	}
	
	private int slot;
	
	public int getSlot() {
		return this.slot;
	}
	
	public void setSlot(int slot) {
		this.slot = slot;
	}

	/**
	 * Sets the amount of the item.
	 */
	public Item setAmount(int amount) {
		this.amount = amount;
		return this;
	}
	
	public void increaseAmount(int amount) {
		this.amount += amount;
	}
	
	public void setItemAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * Gets item's definition.
	 */
	public ItemDefinition getDefinition() {
		return ItemDefinition.forId(id);
	}

	public boolean tradeable(Player player) {
		String name = getDefinition().getName().toLowerCase();
		
		if(name.contains("extreme"))
			return false;
		if(name.toLowerCase().contains("(deg)") || name.toLowerCase().contains("brawling"))
			return false;
		if(name.toLowerCase().contains("Primal"))
			return false;
		for(int i : GameSettings.UNTRADEABLE_ITEMS){
			if(id == i)
				return false;
		}
		if(Effigies.isEffigy(id))
			return false;

		if(player != null) {
			if(!getDefinition().isTradeableByUsername(player.getName())) {
				return false;
			}
		}
		return true;
	}

	public boolean sellable() {
		String name = getDefinition().getName().toLowerCase();
	
		if(name.contains("overload") || name.contains("extreme"))
			return false;
		if(name.toLowerCase().contains("(deg)") || name.toLowerCase().contains("brawling"))
			return false;
		if(name.toLowerCase().contains("Primal"))
			return false;
		for(int i : GameSettings.UNSELLABLE_ITEMS){
			if(id == i)
				return false;
		}
		if(Effigies.isEffigy(id))
			return false;
		return true;
	}

	public static Item getNoted(int id, int amount) {
		int notedItem = id+1;
		if(ItemDefinition.forId(notedItem).getName().equals(ItemDefinition.forId(id).getName())) {
			return new Item(notedItem, amount);
		}
		return new Item(id, amount);
	}

	public static int getUnNoted(int id) {
		int unNoted = id - 1;
		if(id == 11284 || id == 11285) {
			unNoted = 11283;
		}
		if(ItemDefinition.forId(unNoted).getName().equals(ItemDefinition.forId(id).getName())) {
			return unNoted;
		}
		return id;
	}

	public static boolean tradeable(int item) {
		return new Item(item).tradeable(null);
	}
	
	public static boolean sellable(int item) {
		return new Item(item).sellable();
	}
	
	/**
	 * Copying the item by making a new item with same values.
	 */
	public Item copy() {
		return new Item(id, amount);
	}

	/**
	 * Increment the amount by 1.
	 */
	public void incrementAmount() {
		if ((amount + 1) > Integer.MAX_VALUE) {
			return;
		}
		amount++;
	}

	/**
	 * Decrement the amount by 1.
	 */
	public void decrementAmount() {
		if ((amount - 1) < 0) {
			return;
		}
		amount--;
	}

	/**
	 * Increment the amount by the specified amount.
	 */
	public void incrementAmountBy(int amount) {
		if ((this.amount + amount) > Integer.MAX_VALUE) {
			this.amount = Integer.MAX_VALUE;
		} else {
			this.amount += amount;
		}
	}

	/**
	 * Decrement the amount by the specified amount.
	 */
	public void decrementAmountBy(int amount) {
		if ((this.amount - amount) < 1) {
			this.amount = 0;
		} else {
			this.amount -= amount;
		}
	}
	
	/** ITEM RARITY **/
	public ItemRarity rarity;

	public Item setRarity(ItemRarity rarity) {
		this.rarity = rarity;
		return this;
	}
}