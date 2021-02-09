package com.zyrox.world.content.auction_house.item;

import java.util.HashMap;

import com.zyrox.util.Stopwatch;
import com.zyrox.world.content.auction_house.AuctionHouseTimeOption;

/**
 * Created by Jonny on 9/6/2019
 **/
public class AuctionHouseItem {

    private String owner;

    private int itemId;

    private int amount;

    private AuctionHouseTimeOption timeOption;

    private long buyPrice;

    private long auctionPrice;

    private Stopwatch timeSelling = new Stopwatch().reset();

    private HashMap<String, Long> allBids = new HashMap<>();

    private int totalBids;

    private String highestBidder;

    private AuctionHouseItemState state = AuctionHouseItemState.STALE;

    private boolean sentEndingSoonMessage = false;

    public AuctionHouseItem(String owner, int itemId, int amount, AuctionHouseTimeOption timeOption) {
        this.owner = owner;
        this.itemId = itemId;
        this.amount = amount;
        this.timeOption = timeOption;
    }

    public int getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }

    public long getBuyPrice() {
        return buyPrice;
    }

    public long getAuctionPrice() {
        return auctionPrice;
    }

    public AuctionHouseTimeOption getTimeOption() {
        return timeOption;
    }

    public Stopwatch getTimeSelling() {
        return timeSelling;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setBuyPrice(long buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setAuctionPrice(long auctionPrice) {
        this.auctionPrice = auctionPrice;
    }

    public void setTimeOption(AuctionHouseTimeOption timeOption) {
        this.timeOption = timeOption;
        this.timeSelling.reset();
    }

    public String getOwner() {
        return owner;
    }

    public HashMap<String, Long> getAllBids() {
        return allBids;
    }

    public int getTotalBids() {
        return totalBids;
    }

    public void setTotalBids(int totalBids) {
        this.totalBids = totalBids;
    }

    public boolean ended() {
        return getTimeSelling().elapsed(getTimeOption().getDuration());
    }

    public boolean closeToEnding() {
        return getTimeSelling().elapsed(getTimeOption().getDuration() - 60_000);
    }

    public String getHighestBidder() {
       return this.highestBidder;
    }

    public void setHighestBidder(String highestBidder) {
        this.highestBidder = highestBidder;
    }

    public AuctionHouseItemState getState() {
        return state;
    }

    public void setState(AuctionHouseItemState state) {
        this.state = state;
    }

    public boolean isSentEndingSoonMessage() {
        return sentEndingSoonMessage;
    }

    public void setSentEndingSoonMessage(boolean sentEndingSoonMessage) {
        this.sentEndingSoonMessage = sentEndingSoonMessage;
    }
}
