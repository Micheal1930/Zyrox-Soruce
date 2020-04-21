package com.varrock.world.content.gamble;

import java.util.HashMap;
import java.util.Map;

import com.varrock.model.item.Items;
import com.varrock.util.Misc;

/**
 * The items that can be gambled.
 *
 * @author Gabriel || Wolfsdarker
 */
public enum GambleItems {

    COINS(100, "200b", Items.COINS_1),
    
    SCYTHE_OF_VITUR(90, "1", Items.SCYTHE_OF_VITUR),
    
    CELESTIAL_HOOD(100, "1", Items.CELESTIAL_HOOD),
    
    SAGITTARIAN_COIF(90, "1", Items.SAGITTARIAN_COIF),
    SAGITTARIAN_BODY(90, "1", Items.SAGITTARIAN_BODY),
    SAGITTARIAN_CHAPS(90, "1", Items.SAGITTARIAN_CHAPS),
    
    JUSTICIAR_CHESTGUARD(100, "1", Items.JUSTICIAR_CHESTGUARD),
    JUSTICIAR_LEGGUARDS(100, "1", Items.JUSTICIAR_LEGGUARDS),
    JUSTICIAR_FACEGUARD(100, "1", Items.JUSTICIAR_FACEGUARD),
    
    ANCESTRAL_HAT(100, "1", Items.ANCESTRAL_HAT),
    ANCESTRAL_ROBE_TOP(100, "1", Items.ANCESTRAL_ROBE_TOP),
    ANCESTRAL_ROBE_BOTTOM(100, "1", Items.ANCESTRAL_ROBE_BOTTOM),
    
    $100_SCROLL(100, "1", Items.$100_SCROLL),
    $50_SCROLL(100, "1", Items.$50_SCROLL),
    $20_SCROLL(100, "1", Items.$20_SCROLL),
    $10_SCROLL(100, "1", Items.$10_SCROLL),
    
    TWISTED_BOW(100, "1", Items.TWISTED_BOW),
    TWISTED_BOW_2(100, "1", Items.TWISTED_BOW_2),
    TWISTED_BOW_3(80, "1", Items.HALLOWEEN_TWISTED_BOW),
    

    ;

    /**
     * The max roll for this item.
     */
    private final int maxRoll;

    /**
     * The max amount that can be gambled as string.
     */
    private final String maxAmount;

    /**
     * The items that belong to this gamble section.
     */
    private final int[] itemIds;

    /**
     * Constuctor for the gamble items.
     *
     * @param maxRoll
     * @param maxAmount
     * @param itemIDs
     */
    GambleItems(int maxRoll, String maxAmount, int... itemIDs) {
        this.maxRoll = maxRoll;
        this.maxAmount = maxAmount;
        this.itemIds = itemIDs;
    }

    /**
     * Returns the max amount of that item that can be bet at a time.
     *
     * @return the max amount
     */
    public String getMaxBetAmountToString() {
        return maxAmount;
    }

    /**
     * Returns the item's max roll.
     *
     * @return the roll
     */
    public int getMaxRoll() {
        return maxRoll;
    }

    /**
     * Returns the max amount of that item that can be bet at a time.
     *
     * @return the max amount
     */
    public long getMaxBetAmount() {
        return Misc.stringToLong(maxAmount);
    }

    /**
     * Returns the items that are part of this gamble item.
     *
     * @return the item's ids
     */
    public int[] getItemIds() {
        return itemIds;
    }

    /**
     * The items that can be gambled.
     */
    private static final Map<Integer, GambleItems> items = new HashMap<>();

    static {
        for (GambleItems value : values()) {
            for (int itemId : value.getItemIds()) {
                items.put(itemId, value);
            }
        }
    }

    /**
     * Returns the gamble item by searching its id.
     *
     * @param itemId
     * @return the gamble Item
     */
    public static GambleItems get(int itemId) {
        return items.get(itemId);
    }
}
