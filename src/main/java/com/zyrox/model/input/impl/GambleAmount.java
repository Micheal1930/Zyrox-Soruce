package com.zyrox.model.input.impl;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.Graphic;
import com.zyrox.model.Item;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.model.input.EnterAmount;
import com.zyrox.model.item.Items;
import com.zyrox.util.Misc;
import com.zyrox.world.content.PlayerLogs;
import com.zyrox.world.content.gamble.GambleItems;
import com.zyrox.world.entity.impl.player.Player;

public class GambleAmount extends EnterAmount {

    /**
     * The item being gambled.
     */
    private final GambleItems gamble;

    /**
     * Constructor for the amount.
     *
     * @param gamble
     * @param item
     * @param slot
     */
    public GambleAmount(GambleItems gamble, int item, int slot) {
        super(item, slot);
        this.gamble = gamble;
    }

    @Override
    public void handleSyntax(Player player, String amount) {
        long gambleAmount = Misc.stringToLong(amount);
        ItemDefinition def = ItemDefinition.forId(getItem());

        if (gambleAmount <= 0) {
            player.getPacketSender().sendMessage("Invalid amount, please try again.");
            return;
        }

        if (gamble.getMaxBetAmount() < gambleAmount) {
            player.getPacketSender().sendMessage("You can not gamble over " + gamble.getMaxBetAmountToString() + " " + def.getName() + " at a time");
            return;
        }

        if (getItem() != Items.COINS_1) {
            gambleItem(player, gamble, getSlot(), getItem(), (int) gambleAmount);
        } else {
            gambleCash(player, gambleAmount);
        }
    }

    /**
     * Used to gamble items.
     *
     * @param player
     * @param gamble
     * @param itemSlot
     * @param itemId
     * @param amount
     */
    public static void gambleItem(Player player, GambleItems gamble, int itemSlot, int itemId, int amount) {

        ItemDefinition def = ItemDefinition.forId(itemId);
        Item item = player.getInventory().get(itemSlot);


        if (def == null || item.getId() != itemId || item.getAmount() < 1 || amount < 1) {
            return;
        }

        if (item.getAmount() < amount) {
            amount = item.getAmount();
        }

        player.getPacketSender().sendInterfaceRemoval();

        PlayerLogs.log(player.getUsername(), "Player gambled [ " + itemId + " ] " + def.getName() + "x" + Misc.formatLong(amount) + ".");
        player.getPacketSender().sendMessage("Rolling...");
        player.performAnimation(new Animation(11900));
        player.performGraphic(new Graphic(2075));
        player.getInventory().delete(itemId, amount, itemSlot);

        int roll = Misc.random(gamble.getMaxRoll());
        final int betAmount = amount;

        TaskManager.submit(new Task(2, player, false) {
            @Override
            public void execute() {
                if (roll >= 60) {
                    player.forceChat("I Rolled A " + roll + " And Have Won!");
                    player.getInventory().add(new Item(itemId, betAmount * 2));
                } else {
                    player.forceChat("I Rolled A " + roll + " And Have Lost!");
                }
                player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
                player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
                this.stop();
            }
        });
    }

    /**
     * Used to gamble cash from the money pouch.
     *
     * @param player
     * @param amount
     */
    public static void gambleCash(Player player, long amount) {

        ItemDefinition def = ItemDefinition.forId(Items.COINS_1);

        if (player.getMoneyInPouch() < amount) {
            player.getPacketSender().sendMessage("You do not have enough money in your @red@money pouch</col> to gamble that amount.");
            return;
        }

        player.getPacketSender().sendInterfaceRemoval();

        PlayerLogs.log(player.getUsername(), "Player gambled [ " + Items.COINS_1 + " ] " + def.getName() + "x" + Misc.formatLong(amount) + ".");
        player.getPacketSender().sendMessage("Rolling...");
        player.performAnimation(new Animation(11900));
        player.performGraphic(new Graphic(2075));
        player.setMoneyInPouch(player.getMoneyInPouch() - amount);

        int maxRoll = amount >= Misc.stringToLong("20b") ? Misc.getRandom(80) : amount >= Misc.stringToLong("40b") ? Misc.getRandom(75) : amount >= Misc.stringToLong("100b") ? Misc.getRandom(70) : Misc.getRandom(100);
        int roll = Misc.random(maxRoll);
        final long betAmount = amount;

        TaskManager.submit(new Task(2, player, false) {
            @Override
            public void execute() {
                if (roll >= 60) {
                    player.forceChat("I Rolled A " + roll + " And Have Won!");
                    player.setMoneyInPouch(player.getMoneyInPouch() + (betAmount * 2));
                } else {
                    player.forceChat("I Rolled A " + roll + " And Have Lost!");
                }
                player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
                player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
                this.stop();
            }
        });
    }
}
