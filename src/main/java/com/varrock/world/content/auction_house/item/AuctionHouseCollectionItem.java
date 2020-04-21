package com.varrock.world.content.auction_house.item;

/**
 * Created by Jonny on 9/6/2019
 **/
public class AuctionHouseCollectionItem {

    private int itemId;
    private long amount;

    public AuctionHouseCollectionItem(int itemId, long amount) {
        this.itemId = itemId;
        this.amount = amount;
    }

    public int getItemId() {
        return itemId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

}
