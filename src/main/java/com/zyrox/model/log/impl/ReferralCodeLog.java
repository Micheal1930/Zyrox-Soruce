package com.zyrox.model.log.impl;

import com.zyrox.model.log.Log;
import com.zyrox.model.log.LogField;
import com.zyrox.model.log.LogFieldType;
import com.zyrox.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class ReferralCodeLog extends Log {

    private final String username;
    private final String referral;
    private final String ipAddress;
    private final String serial;
    private final String time;

    public ReferralCodeLog(String username, String referral, String ipAddress, String serial, String time) {
        this.username = username;
        this.referral = referral;
        this.ipAddress = ipAddress;
        this.serial = serial;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_REFERRAL_CODES;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("referral", LogFieldType.STRING, referral),
                new LogField("ip_address", LogFieldType.STRING, ipAddress),
                new LogField("serial", LogFieldType.STRING, serial),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
