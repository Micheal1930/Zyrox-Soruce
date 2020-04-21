package com.varrock.world.content.itemtransform;

import java.util.HashMap;
import java.util.Map;

import com.varrock.model.Flag;
import com.varrock.model.Item;
import com.varrock.model.item.Items;
import com.varrock.world.entity.impl.player.Player;

/**
 * The transformation for wielded items.
 *
 * @author Gabriel || Wolfsdarker
 */
public enum ItemTransformation {

    KARAMJAN(1456, Items.MONKEY_GREEGREE),
    ROCKGUARDIAN(23065, Items.USEFUL_ROCK), // 50B XP ITEM

    ;

    /**
     * The npc id to be transformed into.
     */
    private int npcId;

    /**
     * The item to be wielded so the transformation happens.
     */
    private int itemId;

    /**
     * Constructor for the transformation.
     *
     * @param npcId
     * @param itemId
     */
    ItemTransformation(int npcId, int itemId) {
        this.npcId = npcId;
        this.itemId = itemId;
    }

    /**
     * Returns the NPC ID to be transformed into.
     *
     * @return the ID
     */
    public int getNpcId() {
        return npcId;
    }

    /**
     * Returns the item ID to be wielded.
     *
     * @return the ID
     */
    public int getItemId() {
        return itemId;
    }

    private static final Map<Integer, ItemTransformation> transformations = new HashMap<>();

    static {
        for (ItemTransformation value : values()) {
            transformations.put(value.getItemId(), value);
        }
    }

    /**
     * Called when the item is wielded.
     * @param player
     * @param item
     */
    public static void onWieldAction(Player player, Item item, boolean unwielding) {
        if(item == null || item.getId() == -1) {
            return;
        }

        ItemTransformation transform = transformations.get(item.getId());

        if (transform == null) {
            return;
        }

        if(unwielding) {
            for (Item item1 : player.getEquipment().getItems()) {
                if(item1 == null || item.getId() == item.getId())
                    continue;
                ItemTransformation currentTransform = transformations.get(item.getId());

                if(currentTransform != null) {
                    player.setNpcTransformationId(transform.getNpcId());
                    player.getUpdateFlag().flag(Flag.APPEARANCE);
                    return;
                }
            }
            player.setNpcTransformationId(-1);
        } else {
            player.setNpcTransformationId(transform.getNpcId());
        }

        player.getUpdateFlag().flag(Flag.APPEARANCE);
    }
}
