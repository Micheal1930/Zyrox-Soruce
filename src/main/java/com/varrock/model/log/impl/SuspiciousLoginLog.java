package com.varrock.model.log.impl;

import com.varrock.model.log.Log;
import com.varrock.model.log.LogField;
import com.varrock.model.log.LogFieldType;
import com.varrock.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class SuspiciousLoginLog extends Log {

    private final String username;
    private final String ipAddress;
    private final String lastIpAddress;
    private final String serial;
    private final String lastSerial;
    private final String time;

    public SuspiciousLoginLog(String username, String ipAddress, String lastIpAddress, String serial, String lastSerial, String time) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.lastIpAddress = lastIpAddress;
        this.serial = serial;
        this.lastSerial = lastSerial;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_SUSPICIOUS_LOGINS;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("current_ip", LogFieldType.STRING, ipAddress),
                new LogField("last_ip", LogFieldType.STRING, lastIpAddress),
                new LogField("current_serial", LogFieldType.STRING, serial),
                new LogField("last_serial", LogFieldType.STRING, lastSerial),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
