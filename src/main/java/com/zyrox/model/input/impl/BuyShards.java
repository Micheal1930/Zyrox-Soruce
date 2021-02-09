package com.zyrox.model.input.impl;

import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.model.input.EnterAmount;
import com.zyrox.model.item.Items;
import com.zyrox.util.Misc;
import com.zyrox.world.entity.impl.player.Player;

public class BuyShards extends EnterAmount {

    private static final String maxAmount = "1b";

    @Override
    public boolean handleAmount(Player player, int amount) {

        if (amount > Misc.stringToLong(maxAmount)) {
            amount = (int) Misc.stringToLong(maxAmount);
        }

        player.getPacketSender().sendInterfaceRemoval();

        long cost = (long)(ItemDefinition.forId(Items.SPIRIT_SHARDS_2).getValue()) * amount;

        if (player.getMoneyInPouch() < cost) {
            player.getPacketSender().sendMessage("You do not have enough money in your @red@money pouch</col> to buy that amount.");
            return false;
        }

        if (player.getInventory().getFreeSlots() == 0 && !player.getInventory().contains(Items.SPIRIT_SHARDS_2)) {
            player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
            return false;
        }

        int invAmount = player.getInventory().getAmount(Items.SPIRIT_SHARDS_2);
        long total = (long) amount + invAmount;

        if (total > Integer.MAX_VALUE) {
            amount = Integer.MAX_VALUE - invAmount;
            cost = (ItemDefinition.forId(Items.SPIRIT_SHARDS_2).getValue()) * amount;
        }

        player.setMoneyInPouch(player.getMoneyInPouch() - cost);
        player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());

        player.getInventory().add(18016, amount, "Buyshards");
        player.getPacketSender().sendMessage("You've bought " + amount + " Spirit Shards for " + Misc.insertCommasToNumber("" + (int) cost) + " coins.");
        return false;
    }

}
