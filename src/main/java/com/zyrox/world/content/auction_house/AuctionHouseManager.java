package com.zyrox.world.content.auction_house;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zyrox.GameServer;
import com.zyrox.model.Item;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.model.input.Input;
import com.zyrox.model.log.impl.*;
import com.zyrox.util.Misc;
import com.zyrox.util.Stopwatch;
import com.zyrox.world.World;
import com.zyrox.world.content.auction_house.item.AuctionHouseCollectionItem;
import com.zyrox.world.content.auction_house.item.AuctionHouseItem;
import com.zyrox.world.content.auction_house.item.AuctionHouseItemState;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.entity.impl.player.Player;

import java.io.*;
import java.util.*;

import java.lang.reflect.Type;

import org.apache.commons.io.FilenameUtils;

/**
 * Created by Jonny on 9/5/2019
 **/
public class AuctionHouseManager {

    public static HashMap<String, AuctionHouse> AUCTION_HOUSES = new HashMap<String, AuctionHouse>();

    public static HashMap<String, ArrayList<AuctionHouseCollectionItem>> COLLECTION_BOX = new HashMap<>();

    public static void open(Player player) {
        player.auctionHouseViewingState = AuctionHouseViewingState.RANDOM_ITEMS;

        player.getPA().sendInterfaceDisplayState(AuctionHouseConstants.SELL_SCREEN_ID, true);

        updateInventory(player);

        sendRandomItems(player);

        player.getPacketSender().sendInterfaceSet(AuctionHouseConstants.MAIN_SCREEN_ID, AuctionHouseConstants.INVENTORY_SCREEN_ID);
    }

    public static boolean isButton(Player player, int buttonId) {
        if(buttonId >= 33226 && buttonId <= 33366) {
            int index = 21 - ((33366 - buttonId) / 7) - 1;
            selectItem(player, index);
            return true;
        }
        switch(buttonId) {
            case 33132: //open my auctions
                if(player.auctionHouseViewingState == AuctionHouseViewingState.MY_AUCTIONS) {
                    sendRandomItems(player);
                } else {
                    openMyAuctions(player);
                }
                return true;
            case 33505: //remove 1
                updateItemSellingAmount(player, -1);
                return true;
            case 33506: //add 1
                updateItemSellingAmount(player, 1);
                return true;
            case 33515: //cancel sell
                closeSellingPrompt(player);
                return true;
            case 33513: //confirm sell
                confirmItemSellingPrompt(player);
                return true;
            case 33510: //edit bid price
                editStartingBidPrice(player);
                return true;
            case 33511: //edit buy price
                editBuyPrice(player);
                return true;
            case 33512: //edit time
                editTime(player);
                return true;
            case 33129: //close
                player.getPA().sendInterfaceRemoval();
                return true;
            case 33134: //reload
                reload(player);
                return true;
        }
        return false;
    }

    public static void sendRandomItems(Player player) {

        player.auctionHouseItemsViewing.clear();

        List<String> list = new ArrayList<>(AUCTION_HOUSES.keySet());
        Collections.shuffle(list);

        Map<String, AuctionHouse> HOUSES_COPY = new LinkedHashMap<>();

        for(String key : list) {
            HOUSES_COPY.put(key, AUCTION_HOUSES.get(key));
        }

        int total = 0;

        for(Map.Entry entry : HOUSES_COPY.entrySet()) {
            String username = (String) entry.getKey();
            AuctionHouse auctionHouse = (AuctionHouse) entry.getValue();

            if(auctionHouse == null)
                continue;

            if(auctionHouse.getItems().size() <= 0)
                continue;

            ArrayList<AuctionHouseItem> ITEMS = new ArrayList<>();
            for(AuctionHouseItem auctionHouseItem : auctionHouse.getItems()) {
                if(auctionHouseItem == null)
                    continue;
                if(auctionHouseItem.ended())
                    continue;
                if(auctionHouseItem.getOwner().equalsIgnoreCase(player.getName()))
                    continue;
                ITEMS.add(auctionHouseItem);
            }

            int totalItems = ITEMS.size();

            if(totalItems <= 0)
                continue;

            AuctionHouseItem auctionHouseItem = ITEMS.get(Misc.random(totalItems - 1));
            player.auctionHouseItemsViewing.add(auctionHouseItem);

            total++;

            if(total >= AuctionHouseConstants.TOTAL_ITEMS_ALLOWED) {
                break;
            }
        }

        player.getPA().sendAuctionHouseContainer(player.auctionHouseItemsViewing);

        player.auctionHouseViewingState = AuctionHouseViewingState.RANDOM_ITEMS;

        player.getPA().sendString(33132, "My Auctions");

        player.getPA().sendString(33135, "You are viewing random items.");

    }

    public static void selectItem(Player player, int index) {
        if(player.auctionHouseItemsViewing.isEmpty()) {
            player.sendMessage("You are clicking an invalid item. Please refresh.");
            return;
        }

        final AuctionHouseItem auctionHouseItem = player.auctionHouseItemsViewing.get(index);

        boolean owned = auctionHouseItem.getOwner().equalsIgnoreCase(player.getName());

        if(owned) {
            DialogueManager.start(player, new Dialogue() {
                @Override
                public DialogueType type() {
                    return DialogueType.OPTION;
                }

                @Override
                public String[] dialogue() {
                    return new String[] {
                            auctionHouseItem.ended() ? "Clear Auction" : "Close Auction",
                            "Clear All Auctions",
                            "Cancel",

                    };
                }

                @Override
                public boolean action(int option) {

                    player.getPA().closeDialogueOnly();

                    switch(option) {
                        case 1:
                            endAuction(player, auctionHouseItem);
                            return false;
                        case 2:
                            clearAllAuctions(player);
                            return false;
                    }

                    return false;
                }

                @Override
                public boolean closeInterface() {
                    return false;
                }
            });
        } else {
            DialogueManager.start(player, new Dialogue() {
                @Override
                public DialogueType type() {
                    return DialogueType.OPTION;
                }

                @Override
                public String[] dialogue() {
                    return new String[] {
                            "Place Bid",
                            "Buy 1",
                            "Buy X",
                            "Cancel",

                    };
                }

                @Override
                public boolean action(int option) {

                    player.getPA().closeDialogueOnly();

                    switch(option) {
                        case 1:
                            player.setInputHandling(new Input() {
                                @Override
                                public boolean handleAmount(Player player, int amount) {
                                    addBid(player, auctionHouseItem, amount);
                                    return false;
                                }
                            });
                            player.getPacketSender().sendEnterAmountPrompt("Enter bid:");
                            return false;
                        case 2:
                            buy(player, auctionHouseItem, 1);
                            return false;
                        case 3:
                            player.setInputHandling(new Input() {
                                @Override
                                public boolean handleAmount(Player player, int amount) {
                                    buy(player, auctionHouseItem, amount);
                                    return false;
                                }
                            });
                            player.getPacketSender().sendEnterAmountPrompt("Enter amount:");
                            return false;
                    }

                    return false;
                }

                @Override
                public boolean closeInterface() {
                    return false;
                }
            });
        }
    }

    public static void addBid(Player player, AuctionHouseItem auctionHouseItem, long amount) {

        if(player.isIronman()) {
            player.sendMessage("Ironman can't use the auction house.");
            return;
        }

        if(auctionHouseItem.ended() ||
                (auctionHouseItem.getState() != AuctionHouseItemState.STALE
                        && auctionHouseItem.getState() != AuctionHouseItemState.BIDDED_ON)) {
            player.sendMessage("<col=694B00>This item is no longer available to bid on.");
            return;
        }

        if(auctionHouseItem.getAuctionPrice() <= 0) {
            player.sendMessage("<col=694B00>This item is not available for bidding.");
            return;
        }

        if(amount < auctionHouseItem.getAuctionPrice() * 1.01) {
            player.sendMessage("<col=694B00>Your bid price must be 1% higher than the current bid price.");
            return;
        }

        if(amount >= auctionHouseItem.getBuyPrice()) {
            buy(player, auctionHouseItem, 1);
            return;
        }

        boolean hasExistingBid = auctionHouseItem.getAllBids().containsKey(player.getName().toLowerCase());

        boolean useMoneyPouch = false;

        if(amount >= Integer.MAX_VALUE) {
            useMoneyPouch = true;
        } else {
            if (!player.getInventory().contains(new Item(995, (int) amount))) {
                useMoneyPouch = true;
            }
        }

        long moneyInPouch = player.getMoneyInPouch();

        if(hasExistingBid) {

            long existingBid = auctionHouseItem.getAllBids().get(player.getName().toLowerCase());

            moneyInPouch += existingBid;
        }

        if(useMoneyPouch) {
            if(moneyInPouch < amount) {
                player.sendMessage("<col=694B00>You don't have enough coins to bid on this item.");
                return;
            }

            returnAllBids(auctionHouseItem);

            player.removeFromPouch(amount);
        } else {
            if (!player.getInventory().contains(new Item(995, (int) amount))) {
                player.sendMessage("<col=694B00>You don't have enough coins to bid on this item.");
                return;
            }

            returnAllBids(auctionHouseItem);

            player.getInventory().delete(new Item(995, (int) amount));
        }

        auctionHouseItem.setAuctionPrice(amount);
        auctionHouseItem.getAllBids().put(player.getName().toLowerCase(), amount);
        auctionHouseItem.setTotalBids(auctionHouseItem.getTotalBids() + 1);

        auctionHouseItem.setState(AuctionHouseItemState.BIDDED_ON);

        auctionHouseItem.setHighestBidder(player.getName().toLowerCase());

        if(auctionHouseItem.getTimeOption().getDuration() - auctionHouseItem.getTimeSelling().elapsed() <= 10_000) {
            auctionHouseItem.getTimeSelling().setTime(auctionHouseItem.getTimeSelling().getTime() + 10_000);
        }

        AuctionHouse auctionHouse = getAuctionHouseForItem(auctionHouseItem);
        if(auctionHouse != null) {
            saveShop(auctionHouse);
        }

        updateInventory(player);

        refreshAll(auctionHouseItem);

        String timeRemaining = Misc.getTimeLeftForTimer(auctionHouseItem.getTimeOption().getDuration(), auctionHouseItem.getTimeSelling());

        new AuctionHouseBidLog(player.getName(),
                auctionHouseItem.getOwner().toLowerCase(),
                ItemDefinition.forId(auctionHouseItem.getItemId()).getName(),
                auctionHouseItem.getItemId(),
                auctionHouseItem.getTotalBids(),
                auctionHouseItem.getAuctionPrice(),
                timeRemaining,
                Misc.getTime()).submit();

    }

    public static void returnAllBids(AuctionHouseItem auctionHouseItem) {
        for(Map.Entry entry : auctionHouseItem.getAllBids().entrySet()) {
            String username = (String) entry.getKey();
            long bid = (long) entry.getValue();

            Player player = World.getPlayerByName(username);

            if(player == null) {
                addToCollections(username, new AuctionHouseCollectionItem(995, bid));
            } else {
                player.addToPouch(bid);
                player.sendMessage("<col=694B00>Your old bid of "+Misc.insertCommasToNumber(bid)+" coins has been returned to your money pouch.");
            }

            AuctionHouse auctionHouse = getAuctionHouseForItem(auctionHouseItem);
            if(auctionHouse != null) {
                saveShop(auctionHouse);
            }

        }
        auctionHouseItem.getAllBids().clear();
    }

    public static void buy(Player player, AuctionHouseItem auctionHouseItem, int amount) {
        if(player.isIronman()) {
            player.sendMessage("Ironman can't use the auction house.");
            return;
        }

        if(auctionHouseItem.ended()) {
            player.sendMessage("<col=694B00>This item is no longer available to buy.");
            return;
        }

        if(amount > auctionHouseItem.getAmount()) {
            player.sendMessage("<col=694B00>This shop doesn't contain this amount.");
            return;
        }

        long totalPrice = amount * auctionHouseItem.getBuyPrice();

        boolean useMoneyPouch = false;

        if(totalPrice >= Integer.MAX_VALUE) {
            useMoneyPouch = true;
        } else {
            if (!player.getInventory().contains(new Item(995, (int) totalPrice))) {
                useMoneyPouch = true;
            }
        }

        boolean stackable = ItemDefinition.getDefinitions()[auctionHouseItem.getItemId()].isStackable();
        boolean hasItem = player.getInventory().contains(auctionHouseItem.getItemId());
        int freeSlots = player.getInventory().getFreeSlots();
        int amountInInventory = player.getInventory().getAmount(auctionHouseItem.getItemId());

        long total = amountInInventory + amount;

        if(total >= Integer.MAX_VALUE) {
            player.sendMessage("<col=694B00>Please clear some inventory space and try again.");
            return;
        }

        if(!stackable && freeSlots < amount) {
            player.sendMessage("<col=694B00>Please clear some inventory space and try again.");
            return;
        }

        if(useMoneyPouch) {
            if(player.getMoneyInPouch() < totalPrice) {
                player.sendMessage("<col=694B00>You don't have enough coins to buy this item.");
                return;
            }
            player.removeFromPouch(totalPrice);
        } else {
            if (!player.getInventory().contains(new Item(995, (int) totalPrice))) {
                player.sendMessage("<col=694B00>You don't have enough coins to buy this item.");
                return;
            }
            player.getInventory().delete(new Item(995, (int) totalPrice));
        }

        returnAllBids(auctionHouseItem);

        auctionHouseItem.setTotalBids(auctionHouseItem.getTotalBids() + 1);
        auctionHouseItem.setAmount(auctionHouseItem.getAmount() - amount);

        if(auctionHouseItem.getAmount() <= 0) {
            auctionHouseItem.getTimeSelling().setTime(-1);
        }

        addToCollections(auctionHouseItem.getOwner().toLowerCase(), new AuctionHouseCollectionItem(995, (long) ((double)totalPrice * .99)));

        player.getInventory().add(auctionHouseItem.getItemId(), amount);

        auctionHouseItem.setState(AuctionHouseItemState.PURCHASED);

        AuctionHouse auctionHouse = getAuctionHouseForItem(auctionHouseItem);
        if(auctionHouse != null) {
            saveShop(auctionHouse);
        }

        updateInventory(player);

        refreshAll(auctionHouseItem);

        String timeRemaining = Misc.getTimeLeftForTimer(auctionHouseItem.getTimeOption().getDuration(), auctionHouseItem.getTimeSelling());

        new AuctionHousePurchaseLog(player.getName(),
                auctionHouseItem.getOwner().toLowerCase(),
                ItemDefinition.forId(auctionHouseItem.getItemId()).getName(),
                auctionHouseItem.getItemId(),
                amount,
                auctionHouseItem.getAmount(),
                totalPrice,
                timeRemaining,
                Misc.getTime()).submit();

    }

    public static AuctionHouse getAuctionHouseForItem(AuctionHouseItem auctionHouseItem) {
        for(AuctionHouse auctionHouse : AUCTION_HOUSES.values()) {
            if(auctionHouse.getItems().contains(auctionHouseItem))
                return auctionHouse;
        }
        return null;
    }

    public static void refreshAll(AuctionHouseItem auctionHouseItem) {
        for(Player player : World.getPlayers()) {
            if(player == null)
                continue;

            if(player.auctionHouseViewingState != AuctionHouseViewingState.CLOSED) {
                if(player.auctionHouseItemsViewing.contains(auctionHouseItem)) {
                    refresh(player);
                }
            }
        }
    }

    public static void refreshAll() {
        for(Player player : World.getPlayers()) {
            if(player == null)
                continue;

            if(player.auctionHouseViewingState != AuctionHouseViewingState.CLOSED) {
                refresh(player);
            }
        }
    }

    public static void clearAllAuctions(Player player) {
        AuctionHouse auctionHouse = loadHouse(player);

        ArrayList<AuctionHouseItem> auctionHouseItemsToRemove = new ArrayList<>();

        for(AuctionHouseItem auctionHouseItem : auctionHouse.getItems()) {
            if(auctionHouseItem.ended()) {
                if(auctionHouseItem.getAmount() >= 1 &&
                        auctionHouseItem.getState() != AuctionHouseItemState.BIDDED_ON &&
                        auctionHouseItem.getState() != AuctionHouseItemState.CLAIMED) {
                    player.getInventory().add(auctionHouseItem.getItemId(), auctionHouseItem.getAmount());
                    auctionHouseItem.setState(AuctionHouseItemState.CLAIMED);

                    new AuctionHouseClearLog(player.getName(),
                            ItemDefinition.forId(auctionHouseItem.getItemId()).getName(),
                            auctionHouseItem.getItemId(),
                            auctionHouseItem.getAmount(),
                            auctionHouseItem.getTotalBids(),
                            auctionHouseItem.getAuctionPrice(),
                            auctionHouseItem.getBuyPrice(),
                            Misc.getTime()).submit();
                }

                auctionHouseItemsToRemove.add(auctionHouseItem);
            }
        }

        auctionHouse.getItems().removeAll(auctionHouseItemsToRemove);

        updateInventory(player);

        refresh(player);
        openMyAuctions(player);
    }

    public static void endAuction(Player player, AuctionHouseItem auctionHouseItem) {

        AuctionHouse auctionHouse = loadHouse(player);

        if(!auctionHouse.getItems().contains(auctionHouseItem))
            return;

        if(auctionHouseItem.ended()) {

            if(auctionHouseItem.getAmount() >= 1 &&
                    auctionHouseItem.getState() != AuctionHouseItemState.BIDDED_ON &&
                    auctionHouseItem.getState() != AuctionHouseItemState.CLAIMED) {
                player.getInventory().add(auctionHouseItem.getItemId(), auctionHouseItem.getAmount());
                auctionHouseItem.setState(AuctionHouseItemState.CLAIMED);

                new AuctionHouseClearLog(player.getName(),
                        ItemDefinition.forId(auctionHouseItem.getItemId()).getName(),
                        auctionHouseItem.getItemId(),
                        auctionHouseItem.getAmount(),
                        auctionHouseItem.getTotalBids(),
                        auctionHouseItem.getAuctionPrice(),
                        auctionHouseItem.getBuyPrice(),
                        Misc.getTime()).submit();
            }

            auctionHouse.getItems().remove(auctionHouseItem);

            updateInventory(player);

        } else {

            if(auctionHouseItem.getAllBids().size() >= 1) {
                player.sendMessage("<col=694B00>You can't end an auction that has an active bid.");
                return;
            }

            if(auctionHouseItem.closeToEnding()) {
                player.sendMessage("<col=694B00>You can't end an auction that has less than a minute left.");
                return;
            }

            auctionHouseItem.getTimeSelling().setTime(-1);
            auctionHouseItem.getAllBids().clear();

            auctionHouseItem.setState(AuctionHouseItemState.ENDED_BY_OWNER);
        }

        saveShop(auctionHouse);

        refreshAll(auctionHouseItem);
        openMyAuctions(player);
    }

    public static void addToCollections(String name, AuctionHouseCollectionItem item) {

        COLLECTION_BOX.putIfAbsent(name.toLowerCase(), new ArrayList<>());
        COLLECTION_BOX.get(name.toLowerCase()).add(item);

        Player player = World.getPlayerByName(name);
        if(player != null) {
            if(player.lastAuctionCollectionMessageSent.elapsed(30_000)) {
                player.sendMessage("<col=694B00>You have "+item.getAmount()+"x "+ItemDefinition.forId(item.getItemId()).getName()+" pending pickup in your collection box.");
                player.lastAuctionCollectionMessageSent.reset();
            }
        }

        saveCollectionBox(name);
    }

    public static void onLogin(Player player) {
        if(player.isIronman()) {
            return;
        }

        ArrayList<AuctionHouseCollectionItem> COLLECTIONS = COLLECTION_BOX.get(player.getName().toLowerCase());

        if(COLLECTIONS == null || COLLECTIONS.isEmpty()) {
            return;
        }

        player.sendMessage("<col=694B00>You have items pending pickup in your collection box.");
    }

    public static void openMyAuctions(Player player) {
        if(player.isIronman()) {
            player.sendMessage("Ironman can't use the auction house.");
            return;
        }

        player.auctionHouseItemsViewing.clear();

        AuctionHouse auctionHouse = loadHouse(player);

        player.getPA().sendString(33135, "You have "+auctionHouse.getItems().size()+" items selling.");

        ArrayList<AuctionHouseItem> ITEMS = new ArrayList<>(auctionHouse.getItems());

        ITEMS = sort(player, ITEMS);

        player.auctionHouseItemsViewing.addAll(ITEMS);

        player.getPA().sendAuctionHouseContainer(ITEMS);

        player.auctionHouseViewingState = AuctionHouseViewingState.MY_AUCTIONS;
        player.getPA().sendString(33132, "Go Back");
    }

    public static void updateInventory(Player player) {
        player.getPacketSender().sendItemContainer(player.getInventory(), AuctionHouseConstants.INVENTORY_CONTAINER_ID);
    }

    public static void promptItemForSell(Player player, Item item) {

        if(!item.tradeable(player)) {
            player.sendMessage("You can't sell untradeable items here.");
            return;
        }

        if(item.getId() == 995) {
            player.sendMessage("You can't sell coins.");
            return;
        }

        if(item.getId() == 43204) {
            player.sendMessage("You can't sell platinum tokens.");
            return;
        }

        AuctionHouse auctionHouse = loadHouse(player);

        if(auctionHouse.getItems().size() >= AuctionHouseConstants.TOTAL_ITEMS_ALLOWED) {
            player.sendMessage("<col=694B00>Your auction house is only allowed to hold "+AuctionHouseConstants.TOTAL_ITEMS_ALLOWED+" items.");
            return;
        }

        if(!player.getInventory().contains(item)) {
            if(player.getInventory().getAmount(item.getId()) > 0) {
                item.setAmount(player.getInventory().getAmount(item.getId()));
            } else {
                player.sendMessage("<col=694B00>You must have this item in your inventory to sell it.");
                return;
            }
        }

        player.getPA().sendInterfaceDisplayState(AuctionHouseConstants.SELL_SCREEN_ID, false);

        player.auctionHouseItemToSell = new AuctionHouseItem(player.getName().toLowerCase(), item.getId(), item.getAmount(), AuctionHouseTimeOption.HOUR_1);

        updateHouseItemToSell(player);

        editBuyPrice(player);
    }

    public static void updateHouseItemToSell(Player player) {
        AuctionHouseItem houseItem = player.auctionHouseItemToSell;

        if(houseItem == null)
            return;

        player.getPA().sendString(ItemDefinition.forId(houseItem.getItemId()).getName(), 33503);
        player.getPA().sendItemOnInterface(33504, houseItem.getItemId(), houseItem.getAmount());

        player.getPA().sendString("Bid: "+(houseItem.getAuctionPrice() <= 0 ? "Not set" : Misc.insertCommasToNumber(houseItem.getAuctionPrice())), 33507);
        player.getPA().sendString("Buy: "+Misc.insertCommasToNumber(houseItem.getBuyPrice()), 33508);
        player.getPA().sendString(Misc.getTimeLeftForTimer(houseItem.getTimeOption().getDuration(), new Stopwatch().reset()), 33509);

    }

    public static void updateItemSellingAmount(Player player, int amount) {
        AuctionHouseItem houseItem = player.auctionHouseItemToSell;

        if(houseItem == null)
            return;

        long newAmount = houseItem.getAmount() + amount;

        if(newAmount <= 0 || newAmount >= Integer.MAX_VALUE) {
            return;
        }

        if(!player.getInventory().contains(new Item(houseItem.getItemId(), (int) newAmount))) {
            player.sendMessage("<col=694B00>You do not have this amount in your inventory.");
            return;
        }

        if(newAmount > 1) {
            houseItem.setAuctionPrice(0);
        }

        houseItem.setAmount(houseItem.getAmount() + amount);
        updateHouseItemToSell(player);
    }

    public static void closeSellingPrompt(Player player) {
        player.auctionHouseItemToSell = null;
        player.getPA().sendInterfaceDisplayState(AuctionHouseConstants.SELL_SCREEN_ID, true);
    }

    public static void confirmItemSellingPrompt(Player player) {
        if(player.auctionHouseItemToSell == null)
            return;

        if(player.auctionHouseItemToSell.getBuyPrice() <= 0) {
            player.sendMessage("<col=694B00>You must set a buy price before selling your item.");
            return;
        }

        if(player.auctionHouse.getItems().size() >= AuctionHouseConstants.TOTAL_ITEMS_ALLOWED) {
            player.sendMessage("<col=694B00>Your auction house is only allowed to hold "+AuctionHouseConstants.TOTAL_ITEMS_ALLOWED+" items.");
            return;
        }

        Item itemSelling = new Item(player.auctionHouseItemToSell.getItemId(), player.auctionHouseItemToSell.getAmount());

        if(!player.getInventory().contains(itemSelling)) {
            player.sendMessage("<col=694B00>You must have this item in your inventory to sell it.");
            return;
        }

        player.getInventory().delete(itemSelling);

        player.auctionHouseItemToSell.getTimeSelling().reset();

        player.auctionHouse.getItems().add(player.auctionHouseItemToSell);

        String timeRemaining = Misc.getTimeLeftForTimer(player.auctionHouseItemToSell.getTimeOption().getDuration(), new Stopwatch().reset());

        GameServer.discordBot.sendAuctionHouseMessage(itemSelling.getId(), itemSelling.getAmount(), timeRemaining, player.auctionHouseItemToSell.getBuyPrice(), player.auctionHouseItemToSell.getAuctionPrice(), -1, "has been listed for sale");

        new AuctionHousePostLog(player.getName(),
                itemSelling.getDefinition().getName(),
                itemSelling.getId(),
                itemSelling.getAmount(),
                player.auctionHouseItemToSell.getAuctionPrice(),
                player.auctionHouseItemToSell.getBuyPrice(),
                player.auctionHouseItemToSell.getTimeOption().toString(),
                Misc.getTime()).submit();

        saveShop(player.auctionHouse);

        closeSellingPrompt(player);
        updateInventory(player);
        openMyAuctions(player);

    }

    public static void editStartingBidPrice(Player player) {
        if(player.auctionHouseItemToSell.getAmount() > 1) {
            player.sendMessage("<col=694B00>You can only auction one at a time.");
            return;
        }

        player.setInputHandling(new Input() {
            @Override
            public boolean handleAmount(Player player, int amount) {
                if(amount <= 0) {
                    player.sendMessage("<col=694B00>You must set the auction price to something over 0.");
                    return false;
                }
                if(player.auctionHouseItemToSell.getAmount() > 1) {
                    player.sendMessage("<col=694B00>You can only auction one at a time.");
                    return false;
                }

                if(player.auctionHouseItemToSell.getBuyPrice() < amount) {
                    player.auctionHouseItemToSell.setBuyPrice((long) (amount * 1.25));
                }

                player.auctionHouseItemToSell.setAuctionPrice(amount);
                updateHouseItemToSell(player);

                return false;
            }
        });
        player.getPacketSender().sendEnterAmountPrompt("Enter starting bid price:");
    }

    public static void editBuyPrice(Player player) {
        player.setInputHandling(new Input() {
            @Override
            public boolean handleAmount(Player player, int amount) {
                if(amount <= 0) {
                    player.sendMessage("<col=694B00>You must set the buy price to something over 0.");
                    return false;
                }
                if(amount <= player.auctionHouseItemToSell.getAuctionPrice()) {
                    player.sendMessage("<col=694B00>Your buy price must be over your starting bid price.");
                    return false;
                }
                player.auctionHouseItemToSell.setBuyPrice(amount);
                updateHouseItemToSell(player);

                return false;
            }
        });
        player.getPacketSender().sendEnterAmountPrompt("Enter buy price:");
    }

    public static void editTime(Player player) {
        DialogueManager.start(player, new Dialogue() {
            @Override
            public DialogueType type() {
                return DialogueType.OPTION;
            }

            @Override
            public String[] dialogue() {
                return new String[] {
                        "1 hour",
                        "8 hours",
                        "24 hours"
                };
            }

            @Override
            public boolean action(int option) {
                player.getPA().closeDialogueOnly();
                AuctionHouseTimeOption timeOption = null;

                switch(option) {
                    case 1:
                        timeOption = GameServer.isLocal() ? AuctionHouseTimeOption.MINUTE_1 : AuctionHouseTimeOption.HOUR_1;
                        break;
                    case 2:
                        timeOption = AuctionHouseTimeOption.HOUR_8;
                        break;
                    case 3:
                        timeOption = AuctionHouseTimeOption.HOUR_24;
                        break;
                }

                player.auctionHouseItemToSell.setTimeOption(timeOption);
                updateHouseItemToSell(player);

                return false;
            }

            @Override
            public boolean closeInterface() {
                return false;
            }
        });
    }

    public static void searchForItem(Player player, String itemName) {
        player.auctionHouseItemsViewing.clear();

        ArrayList<AuctionHouseItem> ITEMS = new ArrayList<>();

        for(Map.Entry entry : AUCTION_HOUSES.entrySet()) {
            AuctionHouse auctionHouse = (AuctionHouse)entry.getValue();

            for(AuctionHouseItem auctionHouseItem : auctionHouse.getItems()) {

                if(auctionHouseItem == null)
                    continue;

                if(auctionHouseItem.ended())
                    continue;

                ItemDefinition itemDefinition = ItemDefinition.forId(auctionHouseItem.getItemId());

                if(itemDefinition == null)
                    continue;

                if(itemDefinition.getName().contains(itemName)) {
                    ITEMS.add(auctionHouseItem);
                }
            }
        }

        ITEMS = sort(player, ITEMS);

        player.auctionHouseItemsViewing.addAll(ITEMS);

        player.getPA().sendAuctionHouseContainer(ITEMS);

        player.getPA().sendString(33135, "Found "+ITEMS.size()+" result(s) matching '"+itemName+"'");

        player.auctionHouseViewingState = AuctionHouseViewingState.SPECIFIC_ITEM;
        player.lastAuctionItemSearched = itemName;
    }

    public static ArrayList<AuctionHouseItem> sort(Player player, ArrayList<AuctionHouseItem> ITEMS) {
        HashMap<AuctionHouseItem, Long> SORTED_ITEMS = new HashMap<>();

        switch(player.auctionHouseSortType) {
            case PRICE:

                for(AuctionHouseItem auctionHouseItem : ITEMS) {
                    long price = auctionHouseItem.getBuyPrice();
                    if(auctionHouseItem.getAuctionPrice() < price && auctionHouseItem.getAuctionPrice() > 0) {
                        price = auctionHouseItem.getAuctionPrice();
                    }
                    SORTED_ITEMS.put(auctionHouseItem, price);
                }

                SORTED_ITEMS = sortItems(SORTED_ITEMS, true);

                ITEMS.clear();

                ITEMS.addAll(SORTED_ITEMS.keySet());

                break;
            case OLDEST:

                for(AuctionHouseItem auctionHouseItem : ITEMS) {
                    long startTime = auctionHouseItem.getTimeSelling().getTime();

                    SORTED_ITEMS.put(auctionHouseItem, startTime);
                }

                SORTED_ITEMS = sortItems(SORTED_ITEMS, true);

                ITEMS.clear();

                ITEMS.addAll(SORTED_ITEMS.keySet());

                break;
            case NEWEST:

                for(AuctionHouseItem auctionHouseItem : ITEMS) {
                    long startTime = auctionHouseItem.getTimeSelling().getTime();

                    SORTED_ITEMS.put(auctionHouseItem, startTime);
                }

                SORTED_ITEMS = sortItems(SORTED_ITEMS, false);

                ITEMS.clear();

                ITEMS.addAll(SORTED_ITEMS.keySet());

                break;
        }

        return ITEMS;
    }

    public static void pickupCollections(Player player) {

        if(player.isIronman()) {
            player.sendMessage("Ironman can't use the auction house.");
            return;
        }

        ArrayList<AuctionHouseCollectionItem> COLLECTIONS = COLLECTION_BOX.get(player.getName().toLowerCase());
        ArrayList<AuctionHouseCollectionItem> TO_REMOVE = new ArrayList<AuctionHouseCollectionItem>();

        if(COLLECTIONS == null || COLLECTIONS.isEmpty()) {
            player.sendMessage("<col=694B00>You don't have anything in your collection box.");
            return;
        }

        boolean needsSpace = false;

        for(AuctionHouseCollectionItem auctionHouseCollectionItem : COLLECTIONS) {

            int itemId = auctionHouseCollectionItem.getItemId();
            long amount = auctionHouseCollectionItem.getAmount();

            if(amount <= 0) {
                TO_REMOVE.add(auctionHouseCollectionItem);
                continue;
            }

            if (itemId == 995) {
                player.addToPouch(amount);
                auctionHouseCollectionItem.setAmount(0);
                TO_REMOVE.add(auctionHouseCollectionItem);

                new AuctionHouseCollectionLog(player.getName(), "coins", 995, amount, Misc.getTime()).submit();
                continue;
            }

            int existingAmount = player.getInventory().getAmount(itemId);
            boolean stackable = ItemDefinition.forId(itemId).isStackable();

            long amountToCollect = amount;

            if(existingAmount + amountToCollect >= Integer.MAX_VALUE) {
                amountToCollect = Integer.MAX_VALUE - existingAmount;
                needsSpace = true;
            }

            if(auctionHouseCollectionItem.getAmount() - amountToCollect < 0) {
                player.sendMessage("<col=694B00>Something went wrong claiming item "+itemId+",amount="+amount+". Please report to admin.");
                continue;
            }

            if(!stackable && amountToCollect > player.getInventory().getFreeSlots()) {
                amountToCollect = player.getInventory().getFreeSlots();
            }

            player.getInventory().add(itemId, (int) amountToCollect);
            auctionHouseCollectionItem.setAmount(auctionHouseCollectionItem.getAmount() - amountToCollect);

            if(auctionHouseCollectionItem.getAmount() <= 0) {
                TO_REMOVE.add(auctionHouseCollectionItem);
            }

            new AuctionHouseCollectionLog(player.getName(), ItemDefinition.forId(itemId).getName(), itemId, (int) amountToCollect, Misc.getTime()).submit();
        }

        COLLECTIONS.removeAll(TO_REMOVE);

        if(needsSpace) {
            player.sendMessage("<col=694B00>Please clear some inventory space to collect the rest of your items.");
        }

        player.sendMessage("<col=694B00>You have collected all available items in your collection box.");

        saveCollectionBox(player.getName().toLowerCase());

    }

    public static void reload(Player player) {
        switch(player.auctionHouseViewingState) {
            case MY_AUCTIONS:
                openMyAuctions(player);
                break;
            case SPECIFIC_ITEM:
                searchForItem(player, player.lastAuctionItemSearched);
                break;
            case RANDOM_ITEMS:
                sendRandomItems(player);
                break;
        }
    }

    public static void refresh(Player player) {
        player.getPA().sendAuctionHouseContainer(player.auctionHouseItemsViewing);
    }

    public static AuctionHouse loadHouse(Player player) {

        boolean exists = AUCTION_HOUSES.containsKey(player.getName().toLowerCase());

        if(player.auctionHouse == null) {
            player.auctionHouse = exists ? AUCTION_HOUSES.get(player.getName().toLowerCase()) : new AuctionHouse(player.getName().toLowerCase());
        }

        if(!exists)
            AUCTION_HOUSES.put(player.getName().toLowerCase(), player.auctionHouse);

        return player.auctionHouse;
    }

    public static HashMap<AuctionHouseItem, Long> sortItems(HashMap<AuctionHouseItem, Long> hm, boolean ascending) {
        // Create a list from elements of HashMap
        List<Map.Entry<AuctionHouseItem, Long> > list =
                new LinkedList<Map.Entry<AuctionHouseItem, Long> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<AuctionHouseItem, Long> >() {
            public int compare(Map.Entry<AuctionHouseItem, Long> o1,
                               Map.Entry<AuctionHouseItem, Long> o2) {


                return ascending ? (o1.getValue()).compareTo(o2.getValue()) : (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<AuctionHouseItem, Long> temp = new LinkedHashMap<AuctionHouseItem, Long>();
        for (Map.Entry<AuctionHouseItem, Long> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }


    public static void process() {
        refreshAll();

        for(Map.Entry entry : AUCTION_HOUSES.entrySet()) {
            AuctionHouse auctionHouse = (AuctionHouse)entry.getValue();

            for(AuctionHouseItem auctionHouseItem : auctionHouse.getItems()) {

                if(auctionHouseItem == null)
                    continue;

                if(auctionHouseItem.getState() == AuctionHouseItemState.BIDDED_ON) {
                    if(auctionHouseItem.ended()) {

                        String highestBidder = auctionHouseItem.getHighestBidder();
                        long bid = auctionHouseItem.getAllBids().get(auctionHouseItem.getHighestBidder());

                        addToCollections(auctionHouseItem.getOwner().toLowerCase(), new AuctionHouseCollectionItem(995, (long) ((double)bid * .99)));
                        addToCollections(highestBidder.toLowerCase(), new AuctionHouseCollectionItem(auctionHouseItem.getItemId(), 1));

                        auctionHouseItem.setState(AuctionHouseItemState.CLAIMED);

                        auctionHouseItem.setAmount(auctionHouseItem.getAmount() - 1);

                        saveShop(auctionHouse);

                    }
                } else if(auctionHouseItem.getState() == AuctionHouseItemState.STALE) {
                    if(auctionHouseItem.ended()) {

                        addToCollections(auctionHouseItem.getOwner().toLowerCase(), new AuctionHouseCollectionItem(auctionHouseItem.getItemId(), auctionHouseItem.getAmount()));

                        auctionHouseItem.setState(AuctionHouseItemState.CLAIMED);

                        saveShop(auctionHouse);
                    }
                }

                if(auctionHouseItem.getAuctionPrice() > 0) {
                    if(auctionHouseItem.closeToEnding() && !auctionHouseItem.isSentEndingSoonMessage() && !auctionHouseItem.ended()) {

                        String timeRemaining = Misc.getTimeLeftForTimer(auctionHouseItem.getTimeOption().getDuration(), auctionHouseItem.getTimeSelling());

                        GameServer.discordBot.sendAuctionHouseMessage(auctionHouseItem.getItemId(), auctionHouseItem.getAmount(), timeRemaining, auctionHouseItem.getBuyPrice(), auctionHouseItem.getAuctionPrice(), 0, "is ending soon!");

                        auctionHouseItem.setSentEndingSoonMessage(true);
                    }
                }
            }
        }
    }

    public static void saveShop(AuctionHouse auctionHouse) {
        final String ABSOLUTE_FILE_LOCATION = AuctionHouseConstants.AUCTION_HOUSES_SAVES + auctionHouse.getOwner() + ".json";

        try {

            final File DIRECTORY = new File(AuctionHouseConstants.AUCTION_HOUSES_SAVES);

            if (!DIRECTORY.exists()) {
                DIRECTORY.mkdir();
            }

            final File statFile = new File(ABSOLUTE_FILE_LOCATION);

            if (!statFile.exists()) {
                statFile.createNewFile();
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(auctionHouse);
            FileWriter writer = new FileWriter(ABSOLUTE_FILE_LOCATION);

            writer.write(json);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveCollectionBox(String owner) {

        ArrayList<AuctionHouseCollectionItem> collections =  COLLECTION_BOX.get(owner);

        final String ABSOLUTE_FILE_LOCATION = AuctionHouseConstants.AUCTION_COLLECTION_SAVES + owner + ".json";

        try {

            final File DIRECTORY = new File(AuctionHouseConstants.AUCTION_COLLECTION_SAVES);

            if (!DIRECTORY.exists()) {
                DIRECTORY.mkdir();
            }

            final File statFile = new File(ABSOLUTE_FILE_LOCATION);

            if (!statFile.exists()) {
                statFile.createNewFile();
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(collections);
            FileWriter writer = new FileWriter(ABSOLUTE_FILE_LOCATION);

            writer.write(json);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadAll() {
        loadAllShops();
        loadAllCollections();
    }

    public static void loadAllShops() {
        File folder = new File(AuctionHouseConstants.AUCTION_HOUSES_SAVES);
        File[] files = folder.listFiles();

        if(files == null)
            return;

        for(File file : files) {
            if(file == null)
                continue;

            if(file.isFile()) {
                loadShop(file.getName());
            }
        }
    }

    public static void loadAllCollections() {
        File folder = new File(AuctionHouseConstants.AUCTION_COLLECTION_SAVES);
        File[] files = folder.listFiles();

        if(files == null)
            return;

        for(File file : files) {
            if(file == null)
                continue;

            if(file.isFile()) {
                loadCollection(file.getName());
            }
        }
    }

    public static void loadShop(String name) {
        final String ABSOLUTE_FILE_LOCATION = AuctionHouseConstants.AUCTION_HOUSES_SAVES + name;

        try {

            final File DIRECTORY = new File(AuctionHouseConstants.AUCTION_HOUSES_SAVES);

            if (!DIRECTORY.exists()) {
                DIRECTORY.mkdir();
            }

            final File shopFile = new File(ABSOLUTE_FILE_LOCATION);

            if (!shopFile.exists()) {
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(shopFile));
            Type t = new TypeToken<AuctionHouse>() {}.getType();

            AuctionHouse auctionHouse = new Gson().fromJson(br, t);

            String playerName = FilenameUtils.removeExtension(name);

            AUCTION_HOUSES.put(playerName.toLowerCase(), auctionHouse);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadCollection(String name) {
        final String ABSOLUTE_FILE_LOCATION = AuctionHouseConstants.AUCTION_COLLECTION_SAVES + name;

        try {

            final File DIRECTORY = new File(AuctionHouseConstants.AUCTION_COLLECTION_SAVES);

            if (!DIRECTORY.exists()) {
                DIRECTORY.mkdir();
            }

            final File shopFile = new File(ABSOLUTE_FILE_LOCATION);

            if (!shopFile.exists()) {
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(shopFile));
            Type t = new TypeToken<ArrayList<AuctionHouseCollectionItem>>() {}.getType();

            ArrayList<AuctionHouseCollectionItem> ITEMS = new Gson().fromJson(br, t);

            String playerName = FilenameUtils.removeExtension(name);

            COLLECTION_BOX.put(playerName.toLowerCase(), ITEMS);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAll() {
        for(Map.Entry entry : AUCTION_HOUSES.entrySet()) {
            AuctionHouse auctionHouse = (AuctionHouse)entry.getValue();
            if(auctionHouse != null) {
                saveShop(auctionHouse);
            }
        }
    }

}
