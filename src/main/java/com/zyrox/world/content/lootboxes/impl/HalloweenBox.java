package com.zyrox.world.content.lootboxes.impl;

import java.util.ArrayList;
import java.util.List;

import com.zyrox.model.Item;
import com.zyrox.model.item.Items;
import com.zyrox.world.content.lootboxes.LootBox;
import com.zyrox.world.content.lootboxes.LootColumn;
import com.zyrox.world.entity.impl.player.Player;

public class HalloweenBox implements LootBox {

    /**
     * The columns our loot is in
     */
    private List<LootColumn> lootColumns = new ArrayList<>();

    /**
     * The items that will be given when there is no rare drops.
     */
    private List<Item> commonItems = new ArrayList<>();

    /**
     * The items that match the 'uncommon' rarity.
     */
    private List<Item> uncommonItems = new ArrayList<>();

    /**
     * The items that match the 'rare' rarity.
     */
    private List<Item> rareItems = new ArrayList<>();

    /**
     * The items that match the 'super rare' rarity.
     */
    private List<Item> superRareItems = new ArrayList<>();

    /**
     * The items that match the 'ultimate rare' rarity.
     */
    private List<Item> ultimateItems = new ArrayList<>();

    /**
     * Constructor and loader for the box.
     */
    public HalloweenBox() {
        commonItems.add(new Item(Items.ABYSSAL_WHIP, 1));
        commonItems.add(new Item(6059, 1));
        commonItems.add(new Item(50779, 1));
        commonItems.add(new Item(552, 1));
        commonItems.add(new Item(3166, 1));
        commonItems.add(new Item(13157, 1));
        commonItems.add(new Item(43288, 1));
        commonItems.add(new Item(14078, 1));
        commonItems.add(new Item(14079, 1));
        commonItems.add(new Item(14080, 1));
        commonItems.add(new Item(14057, 1));
        commonItems.add(new Item(10156, 1));
        commonItems.add(new Item(9906, 1));
        commonItems.add(new Item(30553, 1));
        commonItems.add(new Item(11789, 1));
        commonItems.add(new Item(6110, 1));
        commonItems.add(new Item(6106, 1));
        commonItems.add(new Item(6914, 1));
        commonItems.add(new Item(51797, 1));
        commonItems.add(new Item(19888, 1));
        commonItems.add(new Item(11780, 1));

        uncommonItems.add(new Item(Items.SKELETON_BOOTS, 1));
        uncommonItems.add(new Item(Items.SKELETON_GLOVES, 1));
        uncommonItems.add(new Item(Items.SKELETON_LEGGINGS, 1));
        uncommonItems.add(new Item(Items.SKELETON_MASK, 1));
        uncommonItems.add(new Item(Items.SKELETON_SHIRT, 1));
        uncommonItems.add(new Item(Items.ANKOU_BOOTS, 1));
        uncommonItems.add(new Item(Items.ANKOU_GLOVES, 1));
        uncommonItems.add(new Item(Items.ANKOU_HOOD, 1));
        uncommonItems.add(new Item(Items.ANKOU_LEGGINGS, 1));
        uncommonItems.add(new Item(Items.ANKOU_ROBE, 1));
        uncommonItems.add(new Item(Items.BANSHEE_KNIFE, 1));
        uncommonItems.add(new Item(Items.BANSHEE_MASK, 1));
        uncommonItems.add(new Item(Items.BANSHEE_ROBE, 1));
        uncommonItems.add(new Item(Items.BANSHEE_TOP, 1));
        uncommonItems.add(new Item(Items.JACK_LANTERN_MASK, 1));

        
        rareItems.add(new Item(3066, 1));
        rareItems.add(new Item(14536, 1));
        rareItems.add(new Item(1591, 1));

        lootColumns.add(LootColumn.RARE);

        lootColumns.add(LootColumn.UNCOMMON);

        lootColumns.add(LootColumn.COMMON);

    }

    @Override
    public List<LootColumn> lootColumns() {
        return lootColumns;
    }

    @Override
    public int freeSlotsRequired() {
        return 1;
    }

    @Override
    public int lootCount(Player player) {
        return 1;
    }

    @Override
    public double uncommonChance(Player player) {
        return 9.0 / 100;
    }

    @Override
    public double rareChance(Player player) {
        return 1.0 / 100;
    }

    @Override
    public double superRareChance(Player player) {
        return -1;
    }

    @Override
    public double ultimateChance(Player player) {
        return -1;
    }

    @Override
    public List<Item> commonItems() {
        return commonItems;
    }

    @Override
    public List<Item> uncommonItems() {
        return uncommonItems;
    }

    @Override
    public List<Item> rareItems() {
        return rareItems;
    }

    @Override
    public List<Item> superRareItems() {
        return superRareItems;
    }

    @Override
    public List<Item> ultimateItems() {
        return ultimateItems;
    }

    @Override
    public String boxName() {
        return "Halloween Box";
    }
}
