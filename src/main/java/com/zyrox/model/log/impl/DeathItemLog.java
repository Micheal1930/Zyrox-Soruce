package com.zyrox.model.log.impl;

import com.zyrox.model.log.Log;
import com.zyrox.model.log.LogField;
import com.zyrox.model.log.LogFieldType;
import com.zyrox.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class DeathItemLog extends Log {

    private final String username;
    private final String killedBy;
    private final String itemName;
    private final int itemId;
    private final int amount;
    private final String location;
    private final String time;

    public DeathItemLog(String username, String killedBy, String itemName, int itemId, int amount, String location, String time) {
        this.username = username;
        this.killedBy = killedBy;
        this.itemName = itemName;
        this.itemId = itemId;
        this.amount = amount;
        this.location = location;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_DEATH_ITEMS_LOST;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("killed_by", LogFieldType.STRING, killedBy),
                new LogField("item_name", LogFieldType.STRING, itemName),
                new LogField("item_id", LogFieldType.INTEGER, itemId),
                new LogField("amount", LogFieldType.INTEGER, amount),
                new LogField("location", LogFieldType.STRING, location),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
