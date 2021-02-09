package com.zyrox.model.log.impl;

import com.zyrox.model.log.Log;
import com.zyrox.model.log.LogField;
import com.zyrox.model.log.LogFieldType;
import com.zyrox.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class TradeItemLog extends Log {

    private final String holder;
    private final String receiver;
    private final String itemName;
    private final int itemId;
    private final int amount;
    private final String time;

    public TradeItemLog(String holder, String receiver, String itemName, int itemId, int amount, String time) {
        this.holder = holder;
        this.receiver = receiver;
        this.itemName = itemName;
        this.itemId = itemId;
        this.amount = amount;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_TRADED_ITEMS;
    }

    public void submit() {
        submit(
                new LogField("holder", LogFieldType.STRING, holder),
                new LogField("receiver", LogFieldType.STRING, receiver),
                new LogField("item_name", LogFieldType.STRING, itemName),
                new LogField("item_id", LogFieldType.INTEGER, itemId),
                new LogField("amount", LogFieldType.INTEGER, amount),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
