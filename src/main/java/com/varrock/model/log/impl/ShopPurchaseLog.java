package com.varrock.model.log.impl;

import com.varrock.model.log.Log;
import com.varrock.model.log.LogField;
import com.varrock.model.log.LogFieldType;
import com.varrock.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class ShopPurchaseLog extends Log {

    private final String username;
    private final String itemName;
    private final int itemId;
    private final int amount;
    private final long price;
    private final String currency;
    private final String time;

    public ShopPurchaseLog(String username, String itemName, int itemId, int amount, long price, String currency, String time) {
        this.username = username;
        this.itemName = itemName;
        this.itemId = itemId;
        this.amount = amount;
        this.price = price;
        this.currency = currency;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_SHOP_PURCHASES;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("item_name", LogFieldType.STRING, itemName),
                new LogField("item_id", LogFieldType.INTEGER, itemId),
                new LogField("amount", LogFieldType.INTEGER, amount),
                new LogField("price", LogFieldType.STRING, price),
                new LogField("currency", LogFieldType.STRING, currency),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
