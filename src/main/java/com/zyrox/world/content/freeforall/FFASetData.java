package com.zyrox.world.content.freeforall;

import com.zyrox.model.Item;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class FFASetData {

    private final Item[] inventoryItems;
    private final Item[] equipmentItems;

    public FFASetData(Item[] inventoryItems, Item[] equipmentItems) {
        this.inventoryItems = inventoryItems;
        this.equipmentItems = equipmentItems;
    }

    public Item[] getInventoryItems() {
        return inventoryItems;
    }

    public Item[] getEquipmentItems() {
        return equipmentItems;
    }
}