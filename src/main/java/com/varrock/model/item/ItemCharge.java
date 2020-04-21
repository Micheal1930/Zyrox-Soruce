package com.varrock.model.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.varrock.model.Item;
import com.varrock.world.entity.impl.player.Player;

/**
 * Handles the item charges for items. (Ex: Dragonfire shield, trident of the seas).
 *
 * @author Gabriel || Wolfsdarker
 */
public class ItemCharge {

    /**
     * The default key for simple charges.
     */
    public static final int DEFAULT_KEY = 0;

    /**
     * The list of charges.
     */
    private final Map<Integer, Integer> charges = new HashMap<>();

    /**
     * The charge's default value.
     */
    private final Map<Integer, Integer> default_values = new HashMap<>();

    /**
     * Constructor for a simple key.
     */
    public ItemCharge() {
        charges.put(DEFAULT_KEY, 0);
        default_values.put(DEFAULT_KEY, 0);
    }

    /**
     * Constructor for a simple key.
     *
     * @param key
     * @param default_value
     */
    public ItemCharge(int key, int default_value) {
        charges.put(key, default_value);
        default_values.put(key, default_value);
    }

    /**
     * Returns the item charges.
     *
     * @return the charges
     */
    public Map<Integer, Integer> getCharges() {
        return charges;
    }

    /**
     * Returns the item charges.
     *
     * @param key
     * @return the charges
     */
    public int getCharges(int key) {
        return charges.getOrDefault(key, -1);
    }

    /**
     * Checks if the item has charges.
     *
     * @param key
     * @return if has charges
     */
    public boolean hasCharges(int key) {
        return charges.containsKey(key);
    }

    /**
     * Increases the amount of charges.
     *
     * @param key
     * @param amount
     */
    public void increaseCharges(int key, int amount) {
        int charge = amount;
        if (charges.containsKey(key)) {
            charge += charges.get(key);
        }
        charges.put(key, charge);
    }

    /**
     * Sets the amount of charges.
     *
     * @param key
     * @param amount
     */
    public void setCharges(int key, int amount) {
        charges.put(key, amount);
    }

    /**
     * Decreases the amount of charges.
     *
     * @param key
     * @param amount
     */
    public void decreaseCharges(int key, int amount) {
        if (!charges.containsKey(key)) {
            return;
        }
        int charge = charges.get(key) - amount;

        if (charge < 0) {
            charge = 0;
        }

        charges.put(key, charge);
    }

    /**
     * Resets the charges to default value.
     *
     * @param key
     */
    public void resetCharge(int key) {
        if (!charges.containsKey(key)) {
            return;
        }
        charges.put(key, default_values.get(key));
    }

    /**
     * Resets all charges to default value.
     */
    public void resetCharges() {
        charges.keySet().forEach(key -> charges.put(key, default_values.getOrDefault(key, 0)));
    }


    /**
     * Handles the death of players with charged items.
     *
     * @param player
     * @param killer
     * @param loot
     */
    public static void onDeath(Player player, Player killer, List<Item> loot) {

        for (int i = 0; i < loot.size(); i++) {

            Item item = loot.get(i);

            if (item == null) {
                continue;
            }

            if (item.getId() == CrawsBow.CRAWS_BOW && CrawsBow.hasCharges(player)) {
                ItemCharge charge = player.getItemCharges().getOrDefault(CrawsBow.CHARGE_KEY, null);

                if (charge != null) {
                    int charges = charge.getCharges(ItemCharge.DEFAULT_KEY);
                    loot.add(Item.of(CrawsBow.REVENANT_ETHER, charges));
                    charge.resetCharges();
                }
            }
        }
    }
}
