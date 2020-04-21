package com.varrock.model.container.impl;

import com.varrock.model.Item;
import com.varrock.model.Locations.Location;
import com.varrock.model.container.ItemContainer;
import com.varrock.model.container.StackType;
import com.varrock.world.content.skill.impl.summoning.SummoningData;
import com.varrock.world.entity.impl.player.Player;

public class BeastOfBurden extends ItemContainer {

    private int capacity;

    public BeastOfBurden(Player player, int capacity) {
        super(player, capacity);
        this.capacity = capacity;
    }

    public BeastOfBurden open() {
        refreshItems();
        return this;
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public StackType stackType() {
        return StackType.DEFAULT;
    }

    @Override
    public BeastOfBurden refreshItems() {

        for (int i = 0; i < SummoningData.frames.length; i++) {
            boolean slotOccupied = i < capacity && getItems()[i].getId() > 0;
            getPlayer().getPacketSender().sendItemOnInterface(SummoningData.getFrameID(i), slotOccupied ? getItems()[i].getId() : -1, 1);
        }

        if (getPlayer().getInterfaceId() != -INTERFACE_ID) {
            getPlayer().getPacketSender().sendInterfaceSet(INTERFACE_ID, 3321);
            getPlayer().getPacketSender().sendItemContainer(getPlayer().getInventory(), 3322);
        }
        return this;
    }

    @Override
    public BeastOfBurden switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
        if (getItems()[slot].getId() != item.getId())
            return this;
        int toFreeSlots = to.getFreeSlots();
        if (toFreeSlots <= 0) {
            to.full();
            return this;
        }
        if(to instanceof Inventory) {
            if (!item.getDefinition().isStackable() && toFreeSlots < item.getAmount()) {
                item.setAmount(toFreeSlots);
            }
        }

        if (getPlayer().getLocation() == Location.FFAARENA || getPlayer().getLocation() == Location.FFALOBBY) {
            return this;
        }
        if (to instanceof BeastOfBurden || to instanceof PriceChecker) {
            if (item.getAmount() > toFreeSlots && !(item.getDefinition().isStackable() && to.contains(item.getId()))) {
                item.setAmount(toFreeSlots);
            }
        }
        if (item.getAmount() <= 0)
            return this;
        delete(item, slot, refresh, to);
        to.add(item, "beast of burden switch");
        if (sort && getAmount(item.getId()) <= 0)
            sortItems();
        if (refresh) {
            refreshItems();
            to.refreshItems();
        }
        return this;
    }

    @Override
    public BeastOfBurden full() {
        getPlayer().getPacketSender().sendMessage("Not enough space in your familiar's inventory.");
        return this;
    }

    public static int beastOfBurdenSlot(int interfaceId) {
        if (interfaceId == 2702)
            return 0;
        else {
            int index = interfaceId - 2704;
            return index >= 1 && index <= 29 ? index : -1;
        }
    }

    public static final int INTERFACE_ID = 2700;
}
