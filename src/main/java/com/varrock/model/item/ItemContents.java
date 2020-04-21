package com.varrock.model.item;

import com.varrock.model.Item;
import com.varrock.world.entity.impl.player.Player;

public class ItemContents {

    /**
     * This array contains the child id where the dialogue
     * statement starts for npc and item dialogues.
     */
    protected static final int[] DIALOGUE_ID = {
            4885,
            4890,
            4896,
            4903
    };

    public static void sendItemOnInterface(Player player, Item item, String title, String... texts) {
        int startDialogueChildId = DIALOGUE_ID[texts.length - 1];
        int headChildId = startDialogueChildId - 2;

        player.getPacketSender().sendInterfaceModel(headChildId, item.getId(), 250);
        player.getPacketSender().sendString(startDialogueChildId - 1, title);

        for (int i = 0; i < texts.length; i++) {
            player.getPacketSender().sendString(startDialogueChildId + i, texts[i]);
        }
        player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);

    }

}
