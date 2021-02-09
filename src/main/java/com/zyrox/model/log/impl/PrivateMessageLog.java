package com.zyrox.model.log.impl;

import com.zyrox.model.log.Log;
import com.zyrox.model.log.LogField;
import com.zyrox.model.log.LogFieldType;
import com.zyrox.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class PrivateMessageLog extends Log {

    private final String username;
    private final String sentTo;
    private final String message;
    private final String time;

    public PrivateMessageLog(String username, String sentTo, String message, String time) {
        this.username = username;
        this.sentTo = sentTo;
        this.message = message;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_PRIVATE_MESSAGES;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("sent_to", LogFieldType.STRING, sentTo),
                new LogField("message", LogFieldType.STRING, message),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
