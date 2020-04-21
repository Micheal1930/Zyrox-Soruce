package com.varrock.model.log.impl;

import com.varrock.model.log.Log;
import com.varrock.model.log.LogField;
import com.varrock.model.log.LogFieldType;
import com.varrock.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class AuctionHouseCollectionLog extends Log {

    private final String username;
    private final String itemName;
    private final int itemId;
    private final long amount;
    private final String time;

    public AuctionHouseCollectionLog(String username, String itemName, int itemId, long amount, String time) {
        this.username = username;
        this.itemName = itemName;
        this.itemId = itemId;
        this.amount = amount;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_AUCTION_COLLECTIONS;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("item_name", LogFieldType.STRING, itemName),
                new LogField("item_id", LogFieldType.INTEGER, itemId),
                new LogField("amount", LogFieldType.STRING, amount),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
