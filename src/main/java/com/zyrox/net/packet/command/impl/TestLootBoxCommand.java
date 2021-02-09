package com.zyrox.net.packet.command.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zyrox.GameSettings;
import com.zyrox.model.Item;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.util.Misc;
import com.zyrox.world.content.lootboxes.LootBox;
import com.zyrox.world.entity.impl.npc.DropViewer;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = {"testbox"}, description = "Tests the loot box!")
public class TestLootBoxCommand extends NameCommand {

    public TestLootBoxCommand() {
        super(GameSettings.SPECIAL_STAFF_NAMES);
    }

    @Override
    public boolean execute(Player player, String[] args) throws Exception {

        int boxId = Integer.parseInt(args[0]);
        int amount = Integer.parseInt(args[1]);

        LootBox box = LootBox.boxes.get(boxId);

        if (box == null) {
            player.sendMessage("There is not box with that ID.");
            return false;
        }

        Map<Integer, Integer> loot_list = new HashMap<>();
        List<Item> items = new ArrayList<>();
        List<String> informations = new ArrayList<>();

        List<Item> lootTable;

        int lootCount = box.lootCount(player);

        if (lootCount < 1) {
            player.sendMessage("This box gives no items.");
            return false;
        }

        for (int i = 0; i < amount; i++) {
            double randomFloat = Misc.randomFloat();

            if (randomFloat <= box.superRareChance(player)) {
                lootTable = box.superRareItems();
            } else if (randomFloat <= box.rareChance(player)) {
                lootTable = box.rareItems();
            } else if (randomFloat <= box.uncommonChance(player)) {
                lootTable = box.uncommonItems();
            } else {
                lootTable = box.commonItems();
            }

            int randomIndex = Misc.random(lootTable.size() - 1);

            if (randomIndex >= lootTable.size()) {
                randomIndex = lootTable.size() - 1;
            }

            Item loot = lootTable.get(randomIndex);

            int current = loot.getAmount();

            if (loot_list.containsKey(loot.getId())) {
                current += loot_list.get(loot.getId());
            }

            loot_list.put(loot.getId(), current);
        }

        for (int item_id : loot_list.keySet()) {
            Item loot = new Item(item_id, loot_list.get(item_id));
            items.add(loot);
            informations.add("@gre@" + loot.getDefinition().getName());
            informations.add("ID: @whi@" + item_id);
        }

        DropViewer.sendItemInfoInterface(player, "Loot for " + amount + " '" + ItemDefinition.forId(boxId).getName() + "'", items, informations);

        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[]{"::testbox"};
    }

}

