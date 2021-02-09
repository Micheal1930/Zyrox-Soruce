package com.zyrox.model.log.impl;

import com.zyrox.model.log.Log;
import com.zyrox.model.log.LogField;
import com.zyrox.model.log.LogFieldType;
import com.zyrox.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class AuctionHouseBidLog extends Log {

    private final String username;
    private final String itemOwner;
    private final String itemName;
    private final int itemId;
    private final int totalBids;
    private final long auctionPrice;
    private final String timeRemaining;
    private final String timeBidded;

    public AuctionHouseBidLog(String username, String itemOwner, String itemName, int itemId, int totalBids, long auctionPrice, String timeRemaining, String timeBidded) {
        this.username = username;
        this.itemOwner = itemOwner;
        this.itemName = itemName;
        this.itemId = itemId;
        this.totalBids = totalBids;
        this.auctionPrice = auctionPrice;
        this.timeRemaining = timeRemaining;
        this.timeBidded = timeBidded;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_AUCTION_BIDS;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("item_owner", LogFieldType.STRING, itemOwner),
                new LogField("item_name", LogFieldType.STRING, itemName),
                new LogField("item_id", LogFieldType.INTEGER, itemId),
                new LogField("total_bids", LogFieldType.INTEGER, totalBids),
                new LogField("auction_price", LogFieldType.STRING, auctionPrice),
                new LogField("time_remaining", LogFieldType.STRING, timeRemaining),
                new LogField("time_bidded", LogFieldType.STRING, timeBidded)
        );
    }

}
