package com.zyrox.model.item;

import com.zyrox.model.Item;
import com.zyrox.model.container.impl.Bank;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.util.Misc;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Handles the ring of bosses effects.
 *
 * @author Gabriel || Wolfsdarker
 */
public class RingOfBosses {

    /**
     * The ring's item ID.
     */
    public static final int ID = 42603;

    /**
     * The location that the loot will be stored.
     */
    public enum CollectionLocation {

        /**
         * Regular location, will drop under the NPC.
         */
        GROUND,

        /**
         * Loot will be added to inventory.
         */
        INVENTORY,

        /**
         * Loot will be added to bank.
         */
        BANK

    }

    /**
     * Returns if the player is wearing the ring.
     *
     * @param player
     * @return if the ring is worn
     */
    public static boolean isWearing(Player player) {
        return player.getEquipment().get(Equipment.RING_SLOT).getId() == ID;
    }

    /**
     * Collects the loot for the player.
     *
     * @param player
     * @param loot
     * @return
     */
    public static boolean collectLoot(Player player, Item loot) {

        if (!isWearing(player)) {
            return false;
        }

        if (player.getRingOfBossesCollection() == CollectionLocation.GROUND) {
            return false;
        }

        switch (player.getRingOfBossesCollection()) {
            case BANK:

                if (loot.getDefinition().isNoted()) {
                    ItemDefinition def = ItemDefinition.forId(loot.getId() - 1);
                    if (def.getName().equalsIgnoreCase(loot.getDefinition().getName())) {
                        loot.setId(loot.getId() - 1);
                    }
                }

                int tabId = Bank.getTabForItem(player, loot.getId());

                if (tabId == -1) {
                    for (int i = 0; i < player.getBanks().length; i++) {
                        if (player.getBank(i).getFreeSlots() > 1) {
                            tabId = i;
                        }
                    }
                }
                
                if (tabId == -1 && loot.getId() != 995) {
                    player.sendMessage("Your bank is full and your ring of bosses cannot collect loot.");
                    return false;
                }
                
				if (loot.getId() == 995 && (tabId == -1 || !Misc.canAddInteger(player.getBank(tabId).getAmount(loot.getId()), loot.getAmount()))) {
					player.setMoneyInPouch(player.getMoneyInPouch() + loot.getAmount());
					player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch() + "");
					return true;
				}

                player.getBank(tabId).add(loot);
                return true;

            case INVENTORY:
                if (player.getInventory().getFreeSlots() < (loot.getDefinition().isNoted() || loot.getDefinition().isStackable() ? 1 :
                        loot.getAmount())) {
                    player.sendMessage("You don't have enough space in your inventory for the ring of bosses to operate.");
                    return false;
                }
                player.getInventory().add(loot);
                return true;
        }

        return false;
    }

    /**
     * Sends the ring's config.
     *
     * @param player
     */
    public static void sendConfig(Player player) {

        if (player.getRingOfBossesCollection() == CollectionLocation.GROUND) {
            player.getPacketSender().sendMessage("<img=10> <col=996633>Your Ring is not collecting items.");
            return;
        }

        player.getPacketSender().sendMessage("<img=10> <col=996633>Your Ring is collecting items and sending them to your " + player.getRingOfBossesCollection().name().toLowerCase() + ".");
    }

    /**
     * Toggles the rings configuration.
     *
     * @param player
     */
    public static void toggleConfig(Player player) {

        int index = player.getRingOfBossesCollection().ordinal() + 1;

        if (index >= CollectionLocation.values().length) {
            index = 0;
        }

        player.setRingOfBossesColletion(CollectionLocation.values()[index]);

        sendConfig(player);
    }

}
