package com.zyrox.model.log.impl;

import com.zyrox.model.log.Log;
import com.zyrox.model.log.LogField;
import com.zyrox.model.log.LogFieldType;
import com.zyrox.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class AuctionHousePurchaseLog extends Log {

    private final String username;
    private final String itemOwner;
    private final String itemName;
    private final int itemId;
    private final long amountPurchased;
    private final long amountLeft;
    private final long purchasePrice;
    private final String timeRemaining;
    private final String timePurchased;

    public AuctionHousePurchaseLog(String username, String itemOwner, String itemName, int itemId, long amountPurchased, long amountLeft, long purchasePrice, String timeRemaining, String timePurchased) {
        this.username = username;
        this.itemOwner = itemOwner;
        this.itemName = itemName;
        this.itemId = itemId;
        this.amountPurchased = amountPurchased;
        this.amountLeft = amountLeft;
        this.purchasePrice = purchasePrice;
        this.timeRemaining = timeRemaining;
        this.timePurchased = timePurchased;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_AUCTION_PURCHASES;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("item_owner", LogFieldType.STRING, itemOwner),
                new LogField("item_name", LogFieldType.STRING, itemName),
                new LogField("item_id", LogFieldType.INTEGER, itemId),
                new LogField("amount_purchased", LogFieldType.STRING, amountPurchased),
                new LogField("amount_left", LogFieldType.STRING, amountLeft),
                new LogField("purchase_price", LogFieldType.STRING, purchasePrice),
                new LogField("time_remaining", LogFieldType.STRING, timeRemaining),
                new LogField("time_purchased", LogFieldType.STRING, timePurchased)
        );
    }

}
