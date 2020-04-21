package com.varrock.model.log.impl;

import com.varrock.model.log.Log;
import com.varrock.model.log.LogField;
import com.varrock.model.log.LogFieldType;
import com.varrock.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class YellMessageLog extends Log {

    private final String username;
    private final String message;
    private final String time;

    public YellMessageLog(String username, String message, String time) {
        this.username = username;
        this.message = message;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_YELL_MESSAGES;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("message", LogFieldType.STRING, message),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
