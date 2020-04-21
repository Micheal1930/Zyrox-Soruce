package com.varrock.model.log.impl;

import com.varrock.model.log.Log;
import com.varrock.model.log.LogField;
import com.varrock.model.log.LogFieldType;
import com.varrock.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class ClanMessageLog extends Log {

    private final String username;
    private final String clan;
    private final String message;
    private final String time;

    public ClanMessageLog(String username, String clan, String message, String time) {
        this.username = username;
        this.clan = clan;
        this.message = message;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_CLAN_MESSAGES;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("clan", LogFieldType.STRING, clan),
                new LogField("message", LogFieldType.STRING, message),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
