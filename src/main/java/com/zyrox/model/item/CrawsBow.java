package com.zyrox.model.item;

import com.zyrox.model.Item;
import com.zyrox.model.input.impl.EnterRevenantEtherAmount;
import com.zyrox.util.Misc;
import com.zyrox.world.entity.impl.player.Player;

/**
 * The craw's bow charging/uncharging.
 *
 * @author Gabriel || Wolfsdarker
 */
public class CrawsBow {

    /**
     * The bow's item ID.
     */
    public static final int CRAWS_BOW = 52550;

    /**
     * The ether's item ID.
     */
    public static final int REVENANT_ETHER = 51820;

    /**
     * The maximum amount of charges for the bracelet.
     */
    public static final int MAX_CHARGES = 1000;

    /**
     * The player charge's list key.
     */
    public static final int CHARGE_KEY = CRAWS_BOW;


    /**
     * Checks if the player is wielding the bracelet.
     *
     * @param player
     * @return if is wielding
     */
    public static boolean isWielding(Player player) {
        return player.getEquipment().contains(CRAWS_BOW);
    }

    /**
     * Checks if the player has charges on the bracelet.
     *
     * @param player
     * @return if has charges
     */

    public static boolean hasCharges(Player player) {
        if (player.getItemCharges().containsKey(CHARGE_KEY)) {

            ItemCharge charge = player.getItemCharges().get(CHARGE_KEY);

            if (charge.getCharges(ItemCharge.DEFAULT_KEY) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Decreases a charge from the bracelet of the seas.
     *
     * @param player
     */
    public static void decreaseCharge(Player player) {
        if (player.getItemCharges().containsKey(CHARGE_KEY)) {

            ItemCharge charge = player.getItemCharges().get(CHARGE_KEY);

            charge.decreaseCharges(ItemCharge.DEFAULT_KEY, 1);

            player.getItemCharges().put(CHARGE_KEY, charge);
        }
    }

    /**
     * Handles the action to check the bracelet.
     *
     * @param player
     */
    public static void check(Player player) {

        ItemCharge charge = player.getItemCharges().getOrDefault(CHARGE_KEY, null);

        int chargeCount = 0;

        if (charge != null) {
            chargeCount = charge.getCharges(ItemCharge.DEFAULT_KEY);
        }

        final String text = chargeCount == 0
                ? "Your bow is currently not charged."
                : "Your bow currently holds @dre@" + Misc.format(chargeCount) + "@bla@ charges.";

        ItemContents.sendItemOnInterface(player, new Item(CRAWS_BOW), "Inspecting Charges...", text);
    }

    /**
     * Loads the player's bow with charges.
     *
     * @param player
     */
    public static void load(Player player, Item target, Item source) {

        if (source.getId() != REVENANT_ETHER && target.getId() != REVENANT_ETHER) {
            return;
        }

        player.setInputHandling(new EnterRevenantEtherAmount());
        player.getPacketSender().sendEnterAmountPrompt("How many charges would you like to load?");
    }

    /**
     * Loads the player's bow with charges.
     *
     * @param player
     */
    public static boolean load(Player player, int amount) {

        if (amount < 1) {
            return false;
        }

        int player_charges = player.getInventory().getAmount(REVENANT_ETHER);

        if (player_charges <= 0) {
            player.sendMessage("You don't have the required items to charge this item.");
            return true;
        }

        if (player_charges < amount) {
            amount = player_charges;
        }

        ItemCharge charge = player.getItemCharges().getOrDefault(CHARGE_KEY, null);

        int current_charges = 0;

        if (charge != null) {
            current_charges = charge.getCharges(ItemCharge.DEFAULT_KEY);
        } else {
            charge = new ItemCharge();
        }

        if (current_charges + amount > MAX_CHARGES) {
            amount = MAX_CHARGES - current_charges;
        }

        player.getInventory().delete(REVENANT_ETHER, amount);

        charge.increaseCharges(ItemCharge.DEFAULT_KEY, amount);

        player.getItemCharges().put(CHARGE_KEY, charge);

        ItemContents.sendItemOnInterface(player, new Item(CRAWS_BOW), "Injecting Charges...", "You've charged your bracelet with @dre@"
                + Misc.format(amount) + " </col>revenant ethers.");

        check(player);
        return true;
    }

    /**
     * Uncharges the bracelet.
     *
     * @param player
     */
    public static void uncharge(Player player) {

        ItemCharge charge = player.getItemCharges().getOrDefault(CHARGE_KEY, null);

        if (charge == null) {
            player.sendMessage("This bracelet is already uncharged.");
            return;
        }

        int charges = charge.getCharges(ItemCharge.DEFAULT_KEY);

        player.getInventory().add(REVENANT_ETHER, charges);

        charge.resetCharges();

        check(player);
    }
}
