package com.zyrox.model.log.impl;

import com.zyrox.model.log.Log;
import com.zyrox.model.log.LogField;
import com.zyrox.model.log.LogFieldType;
import com.zyrox.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class GlobalEventLog extends Log {

    private final String event;
    private final String description;
    private final String time;

    public GlobalEventLog(String event, String description, String time) {
        this.event = event;
        this.description = description;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_GLOBAL_EVENTS;
    }

    public void submit() {
        submit(
                new LogField("event", LogFieldType.STRING, event),
                new LogField("description", LogFieldType.STRING, description),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
