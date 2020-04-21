package com.varrock.model.log.impl;

import com.varrock.model.log.Log;
import com.varrock.model.log.LogField;
import com.varrock.model.log.LogFieldType;
import com.varrock.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class RareLootLog extends Log {

    private final String username;
    private final String itemName;
    private final int itemId;
    private final int amount;
    private final String receivedFrom;
    private final String time;

    public RareLootLog(String username, String itemName, int itemId, int amount, String receivedFrom, String time) {
        this.username = username;
        this.itemName = itemName;
        this.itemId = itemId;
        this.amount = amount;
        this.receivedFrom = receivedFrom;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_RARE_LOOT;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("item_name", LogFieldType.STRING, itemName),
                new LogField("item_id", LogFieldType.INTEGER, itemId),
                new LogField("item_amount", LogFieldType.INTEGER, amount),
                new LogField("received_from", LogFieldType.STRING, receivedFrom),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
