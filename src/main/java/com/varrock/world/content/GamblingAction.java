package com.varrock.world.content;

import com.varrock.model.Item;
import com.varrock.model.input.impl.GambleAmount;
import com.varrock.world.content.gamble.GambleItems;
import com.varrock.world.entity.impl.player.Player;

/**
 * Handles the action of gambling an item through the gambling NPC.
 *
 * @author Andys1814.
 */
public class GamblingAction {

    /**
     * Handles the action of using an item on the gambler.
     *
     * @param player
     * @param slot
     * @param item
     */
    public static void handleGambleItem(Player player, int slot, Item item) {

        GambleItems gamble = GambleItems.get(item.getId());

        if (gamble == null) {
            player.sendMessage("You can't gamble this item.");
            return;
        }

        if(gamble.getMaxBetAmount() == 1) {
            GambleAmount.gambleItem(player, gamble, slot, item.getId(), 1);
        } else {
            player.getPacketSender().sendEnterInputPrompt("How many " + item.getDefinition().getName().toLowerCase() + " would you like to gamble?");
            player.setInputHandling(new GambleAmount(gamble, item.getId(), slot));
        }

    }

}
