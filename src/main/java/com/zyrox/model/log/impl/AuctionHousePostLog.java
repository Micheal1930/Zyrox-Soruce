package com.zyrox.model.log.impl;

import com.zyrox.model.log.Log;
import com.zyrox.model.log.LogField;
import com.zyrox.model.log.LogFieldType;
import com.zyrox.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class AuctionHousePostLog extends Log {

    private final String username;
    private final String itemName;
    private final int itemId;
    private final int amount;
    private final long auctionPrice;
    private final long buyPrice;
    private final String timeRemaining;
    private final String timePosted;

    public AuctionHousePostLog(String username, String itemName, int itemId, int amount, long auctionPrice, long buyPrice, String timeRemaining, String timePosted) {
        this.username = username;
        this.itemName = itemName;
        this.itemId = itemId;
        this.amount = amount;
        this.auctionPrice = auctionPrice;
        this.buyPrice = buyPrice;
        this.timeRemaining = timeRemaining;
        this.timePosted = timePosted;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_AUCTION_POSTS;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("item_name", LogFieldType.STRING, itemName),
                new LogField("item_id", LogFieldType.INTEGER, itemId),
                new LogField("amount", LogFieldType.INTEGER, amount),
                new LogField("auction_price", LogFieldType.STRING, auctionPrice),
                new LogField("buy_price", LogFieldType.STRING, buyPrice),
                new LogField("time_remaining", LogFieldType.STRING, timeRemaining),
                new LogField("time_posted", LogFieldType.STRING, timePosted)
        );
    }

}
