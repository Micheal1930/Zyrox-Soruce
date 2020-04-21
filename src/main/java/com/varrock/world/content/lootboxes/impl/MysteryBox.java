package com.varrock.world.content.lootboxes.impl;

import java.util.ArrayList;
import java.util.List;

import com.varrock.model.Item;
import com.varrock.model.item.Items;
import com.varrock.world.content.lootboxes.LootBox;
import com.varrock.world.content.lootboxes.LootColumn;
import com.varrock.world.entity.impl.player.Player;

public class MysteryBox implements LootBox {

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
    public MysteryBox() {
        commonItems.add(new Item(Items.AMULET_OF_GLORY_4, 1));
        commonItems.add(new Item(Items.FIRE_CAPE, 1));
        commonItems.add(new Item(Items.AVAS_ACCUMULATOR, 1));
        commonItems.add(new Item(Items.SARADOMIN_CAPE, 1));
        commonItems.add(new Item(Items.DRAGON_SCIMITAR_OR, 1));
        commonItems.add(new Item(Items.SEERS_RING, 1));
        commonItems.add(new Item(Items.WARRIOR_RING, 1));
        commonItems.add(new Item(Items.ARCHERS_RING, 1));
        commonItems.add(new Item(Items.BERSERKER_RING, 1));
        commonItems.add(new Item(Items.RUNE_PLATEBODY, 1));
        commonItems.add(new Item(Items.RUNE_PLATELEGS, 1));
        commonItems.add(new Item(Items.RUNE_FULL_HELM, 1));
        commonItems.add(new Item(Items.BARROWS_GLOVES, 1));

        uncommonItems.add(new Item(Items.AMULET_OF_FURY, 1));
        uncommonItems.add(new Item(Items.ARCANE_STREAM_NECKLACE, 1));
        uncommonItems.add(new Item(Items.AMULET_OF_RANGING, 1));
        uncommonItems.add(new Item(Items.TOKHAAR_KAL, 1));
        uncommonItems.add(new Item(Items.AVAS_ASSEMBLER, 1));
        uncommonItems.add(new Item(Items.ABYSSAL_WHIP, 1));
        uncommonItems.add(new Item(Items.ABYSSAL_TENTACLE, 1));
        uncommonItems.add(new Item(Items.STAFF_OF_LIGHT, 1));
        uncommonItems.add(new Item(Items.MASTER_WAND, 1));
        uncommonItems.add(new Item(Items.DRAGON_HUNTER_CROSSBOW, 1));
        uncommonItems.add(new Item(Items.DRAGON_DEFENDER, 1));
        uncommonItems.add(new Item(Items.FLAMEBURST_DEFENDER, 1));
        uncommonItems.add(new Item(Items.MAGES_BOOK, 1));
        uncommonItems.add(new Item(Items.DRAGON_BOOTS, 1));
        uncommonItems.add(new Item(Items.BARROWS___KARILS_SET, 1));
        uncommonItems.add(new Item(Items.BARROWS___AHRIM_SET, 1));
        uncommonItems.add(new Item(Items.BARROWS___DHAROK_SET, 1));
        uncommonItems.add(new Item(Items.BARROWS___GUTHAN_SET, 1));
        uncommonItems.add(new Item(Items.BARROWS___TORAG_SET, 1));
        uncommonItems.add(new Item(Items.BARROWS___VERAC_SET, 1));
        uncommonItems.add(new Item(Items.SEERS_RING_I, 1));
        uncommonItems.add(new Item(Items.ARCHERS_RING_I, 1));
        uncommonItems.add(new Item(Items.WARRIOR_RING_I, 1));
        uncommonItems.add(new Item(Items.ZURIELS_HOOD, 1));
        uncommonItems.add(new Item(Items.ZURIELS_ROBE_BOTTOM, 1));
        uncommonItems.add(new Item(Items.ZURIELS_ROBE_TOP, 1));
        uncommonItems.add(new Item(Items.ZURIELS_STAFF, 1));
        uncommonItems.add(new Item(Items.MORRIGANS_COIF, 1));
        uncommonItems.add(new Item(Items.MORRIGANS_JAVELIN, 25));
        uncommonItems.add(new Item(Items.MORRIGANS_LEATHER_BODY, 1));
        uncommonItems.add(new Item(Items.MORRIGANS_LEATHER_CHAPS, 1));
        uncommonItems.add(new Item(Items.MORRIGANS_THROWING_AXE, 25));

        rareItems.add(new Item(Items.AMULET_OF_TORTURE, 1));
        rareItems.add(new Item(Items.ABYSSAL_VINE_WHIP_1, 1));
        rareItems.add(new Item(Items.BERSERKER_RING_I, 1));
        rareItems.add(new Item(Items.VOLCANIC_ABYSSAL_WHIP, 1));
        rareItems.add(new Item(Items.TRIDENT_OF_THE_SEAS, 1));
        rareItems.add(new Item(Items.TOXIC_STAFF_OF_THE_DEAD, 1));
        rareItems.add(new Item(Items.ARMADYL_CROSSBOW, 1));
        rareItems.add(new Item(Items.TOXIC_BLOWPIPE, 1));
        rareItems.add(new Item(Items.TWISTED_BUCKLER, 1));
        rareItems.add(new Item(Items.DRAGONFIRE_SHIELD, 1));
        rareItems.add(new Item(Items.SPECTRAL_SPIRIT_SHIELD, 1));
        rareItems.add(new Item(Items.BANDOS_CHESTPLATE, 1));
        rareItems.add(new Item(Items.BANDOS_TASSETS, 1));
        rareItems.add(new Item(Items.ARMADYL_HELMET, 1));
        rareItems.add(new Item(Items.ARMADYL_CHESTPLATE, 1));
        rareItems.add(new Item(Items.ARMADYL_CHAINSKIRT, 1));
        rareItems.add(new Item(Items.RAGEFIRE_BOOTS, 1));
        rareItems.add(new Item(Items.GLAIVEN_BOOTS, 1));
        rareItems.add(new Item(Items.STEADFAST_BOOTS, 1));
        rareItems.add(new Item(Items.EYE_OF_THE_MAGE, 1));
        rareItems.add(new Item(Items.EYE_OF_THE_WARRIOR, 1));
        rareItems.add(new Item(Items.EYE_OF_THE_RANGE, 1));
        rareItems.add(new Item(Items.ARMADYL_GODSWORD, 1));
        rareItems.add(new Item(Items.DRAGON_CLAWS, 1));
        rareItems.add(new Item(Items.KORASIS_SWORD, 1));
        rareItems.add(new Item(Items.SERPENTINE_HELM, 1));
        rareItems.add(new Item(Items.ELYSIAN_SPIRIT_SHIELD, 1));
        rareItems.add(new Item(Items.ARCANE_SPIRIT_SHIELD, 1));

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
        return 15.0 / 100;
    }

    @Override
    public double rareChance(Player player) {
        return 2.0 / 100;
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
        return "Mystery Box";
    }
}
