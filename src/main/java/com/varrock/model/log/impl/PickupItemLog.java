package com.varrock.model.log.impl;

import com.varrock.model.log.Log;
import com.varrock.model.log.LogField;
import com.varrock.model.log.LogFieldType;
import com.varrock.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class PickupItemLog extends Log {

    private final String username;
    private final String itemName;
    private final int itemId;
    private final int amount;
    private final String location;
    private final String time;

    public PickupItemLog(String username, String itemName, int itemId, int amount, String location, String time) {
        this.username = username;
        this.itemName = itemName;
        this.itemId = itemId;
        this.amount = amount;
        this.location = location;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_PICKUP_ITEMS;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("item_name", LogFieldType.STRING, itemName),
                new LogField("item_id", LogFieldType.INTEGER, itemId),
                new LogField("amount", LogFieldType.INTEGER, amount),
                new LogField("location", LogFieldType.STRING, location),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
