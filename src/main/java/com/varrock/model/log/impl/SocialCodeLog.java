package com.varrock.model.log.impl;

import com.varrock.model.log.Log;
import com.varrock.model.log.LogField;
import com.varrock.model.log.LogFieldType;
import com.varrock.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class SocialCodeLog extends Log {

    private final String username;
    private final String code;
    private final String ipAddress;
    private final String serial;
    private final String time;

    public SocialCodeLog(String username, String code, String ipAddress, String serial, String time) {
        this.username = username;
        this.code = code;
        this.ipAddress = ipAddress;
        this.serial = serial;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_SOCIAL_CODES;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("code", LogFieldType.STRING, code),
                new LogField("ip_address", LogFieldType.STRING, ipAddress),
                new LogField("serial", LogFieldType.STRING, serial),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
