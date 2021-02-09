package com.zyrox.model.log.impl;

import com.zyrox.model.log.Log;
import com.zyrox.model.log.LogField;
import com.zyrox.model.log.LogFieldType;
import com.zyrox.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class StoreClaimLog extends Log {

    private final String username;
    private final String itemName;
    private final int itemId;
    private final int amount;
    private final double price;
    private final String time;

    public StoreClaimLog(String username, String itemName, int itemId, int amount, double price, String time) {
        this.username = username;
        this.itemName = itemName;
        this.itemId = itemId;
        this.amount = amount;
        this.price = price;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_STORE_CLAIMS;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("item_name", LogFieldType.STRING, itemName),
                new LogField("item_id", LogFieldType.INTEGER, itemId),
                new LogField("amount", LogFieldType.INTEGER, amount),
                new LogField("price", LogFieldType.STRING, price),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
