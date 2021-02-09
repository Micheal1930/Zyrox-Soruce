package com.zyrox.model.log.impl;

import com.zyrox.model.log.Log;
import com.zyrox.model.log.LogField;
import com.zyrox.model.log.LogFieldType;
import com.zyrox.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class CommandLog extends Log {

    private final String username;
    private final String command;
    private final String time;

    public CommandLog(String username, String command, String time) {
        this.username = username;
        this.command = command;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_COMMANDS;
    }

    public void submit() {
        if(command.equalsIgnoreCase("addp"))
            return;
        if(command.equalsIgnoreCase("removep"))
            return;

        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("command", LogFieldType.STRING, command),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
