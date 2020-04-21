package com.varrock.world.content.degrade;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import com.varrock.model.Flag;
import com.varrock.model.Item;
import com.varrock.model.container.impl.Equipment;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.model.definitions.WeaponInterfaces;
import com.varrock.world.content.BonusManager;
import com.varrock.world.content.combat.magic.Autocasting;
import com.varrock.world.entity.impl.player.Player;

/**
 * Handles degrading items
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class DegradingManager {

	/**
	 * The armour slots
	 */
	private static final int[] BODY_SLOTS = { Equipment.WEAPON_SLOT, Equipment.HEAD_SLOT, Equipment.LEG_SLOT,
			Equipment.BODY_SLOT, Equipment.RING_SLOT };

	/**
	 * The degrading items
	 */
	private ArrayList<DegradingItem> degradingItems = new ArrayList<DegradingItem>();

	/**
	 * Degrading weapon from attack
	 * 
	 * @param player the player
	 * @param attack on attack
	 */
	public static void degrade(Player player, boolean attack) {
		/*
		 * Loop through the slots
		 */
		for (int slot = 0; slot < BODY_SLOTS.length; slot++) {
			/*
			 * Only attacking or blocking
			 */
			if ((attack && BODY_SLOTS[slot] != Equipment.WEAPON_SLOT)
					|| (!attack && BODY_SLOTS[slot] == Equipment.WEAPON_SLOT)) {
				continue;
			}
			/*
			 * Invalid item
			 */
			if (player.getEquipment().get(BODY_SLOTS[slot]) == null) {
				continue;
			}
			/*
			 * The item
			 */
			Item body = player.getEquipment().get(BODY_SLOTS[slot]);
			/*
			 * The weapon
			 */
			int id = body.getId();
			/*
			 * No weapon
			 */
			if (id < 1) {
				continue;
			}
			/*
			 * The definition
			 */
			final DegradingDataItem degradeData = DegradingItemJSONLoader.getId(id);
			/*
			 * The item
			 */
			if (degradeData == null) {
				continue;
			}
			/*
			 * Check existence
			 */
			if (id != degradeData.getId()) {
				continue;
			}
			/*
			 * The name
			 */
			String name = body.getDefinition().getName();
			/*
			 * Degrades on instant combat
			 */
			if (degradeData.isDegradeOnCombat() && id != degradeData.getNextItem()) {
				/*
				 * Sets the equipment
				 */
				player.getEquipment().set(BODY_SLOTS[slot], new Item(degradeData.getNextItem()));
				/*
				 * Refresh equipment
				 */
				player.getEquipment().refreshItems();
				/*
				 * Refresh weapon
				 */
				if (BODY_SLOTS[slot] == 3) {
					WeaponInterfaces.assign(player);
				}
				/*
				 * Update bonuses
				 */
				BonusManager.update(player);
				/*
				 * Refresh appearance
				 */
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				player.getPacketSender().sendMessage("Your " + name + " has degraded!");
			}
			/*
			 * Already contains
			 */
			if (exists(player, id)) {
				/*
				 * The degrading item
				 */
				Optional<DegradingItem> degrading = getItem(player, id);
				if (!degrading.isPresent()) {
					return;
				}
				/*
				 * Degrading hits
				 */
				degrading.get().decreaseHits();
				/*
				 * Notify
				 */
				if (degrading.get().getHitsRemaining() == 100) {
					player.getPacketSender()
							.sendMessage(degrading.get().getDefinition().getName() + " has about 100 charges left!");
				} else if (degrading.get().getHitsRemaining() < 50) {
					if (degrading.get().getHitsRemaining() % 10 == 0 && degrading.get().getHitsRemaining() != 0) {
						player.getPacketSender().sendMessage(degrading.get().getDefinition().getName() + ": has about "
								+ degrading.get().getHitsRemaining() + " charges left!");
					}
				}
				/*
				 * Finished
				 */
				if (degrading.get().getHitsRemaining() <= 0) {
					/*
					 * Fully degraded
					 */
					if (degradeData.getNextItem() == -1) {
						/*
						 * Reset weapon
						 */
						if (BODY_SLOTS[slot] == Equipment.WEAPON_SLOT) {
							/*
							 * Reset auto cast
							 */
							if (player.getAutocastSpell() != null) {
								Autocasting.resetAutocast(player, true);
							}
							/*
							 * Reset special attack
							 */
							player.setSpecialActivated(false);
						}
						player.getPacketSender().sendMessage("Your " + name + " has fully degraded.");
					} else {
						player.getPacketSender().sendMessage("Your " + name + " has degraded.");
					}
					/*
					 * Degrades item
					 */
					player.getEquipment().set(BODY_SLOTS[slot],
							degradeData.getNextItem() == -1 ? new Item(-1, 0) : new Item(degradeData.getNextItem()));
					WeaponInterfaces.assign(player);
					BonusManager.update(player);
					player.getEquipment().refreshItems();
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					remove(player, id);
				}
				continue;
			}
			/*
			 * Adds to list
			 */
			player.getDegrading().getDegradable().add(DegradingItemJSONLoader.getId(id).toItem());
		}
	}

	/**
	 * Checks the charges on the weapon
	 * 
	 * @param player the player
	 * @param id     the id
	 */
	public static void checkCharge(Player player, int id) {
		/*
		 * The definition
		 */
		DegradingDataItem item = DegradingItemJSONLoader.get(id);
		/*
		 * The item
		 */
		if (item == null) {
			return;
		}
		/*
		 * Already contains
		 */
		if (exists(player, id)) {
			/*
			 * The degrading item
			 */
			Optional<DegradingItem> degrading = getItem(player, id);
			/*
			 * Not present
			 */
			if (!degrading.isPresent()) {
				return;
			}
			/*
			 * The charges left
			 */
			player.getPacketSender().sendMessage(degrading.get().getDefinition().getName() + " has "
					+ degrading.get().getHitsRemaining() + " charges left.");
		} else {
			player.getPacketSender().sendMessage(ItemDefinition.forId(id).getName()
					+ " has not been affected and provides " + item.getHitsRemaining() + " charges.");
		}
	}

	/**
	 * Checks if exists
	 * 
	 * @param player the player
	 * @param id     the id
	 * @return existing
	 */
	private static boolean exists(Player player, int id) {
		return getItem(player, id).isPresent();
	}

	/**
	 * Gets the item by id
	 * 
	 * @param player the player
	 * @param id     the id
	 * @return the item
	 */
	public static Optional<DegradingItem> getItem(Player player, int id) {
		return player.getDegrading().getDegradable().stream().filter(Objects::nonNull)
				.filter(item -> item.getId() == id).findFirst();
	}

	/**
	 * Removing an item from the list
	 * 
	 * @param player the player
	 * @param id     the id
	 */
	public static void remove(Player player, int id) {
		player.getDegrading().getDegradable().trimToSize();
		ArrayList<DegradingItem> items = (ArrayList<DegradingItem>) player.getDegrading().getDegradable().clone();
		for (DegradingItem item : items) {
			if (item == null) {
				continue;
			}
			if (item.getId() == id) {
				player.getDegrading().getDegradable().remove(item);
			}
		}
	}

	/**
	 * Gets the degradable
	 *
	 * @return the degradable
	 */
	public ArrayList<DegradingItem> getDegradable() {
		return degradingItems;
	}
}
