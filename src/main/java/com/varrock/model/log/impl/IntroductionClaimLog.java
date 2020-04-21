package com.varrock.model.log.impl;

import com.varrock.model.log.Log;
import com.varrock.model.log.LogField;
import com.varrock.model.log.LogFieldType;
import com.varrock.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class IntroductionClaimLog extends Log {

    private final String username;
    private final String ipAddress;
    private final String serial;
    private final int topicId;
    private final String time;

    public IntroductionClaimLog(String username, String ipAddress, String serial, int topicId, String time) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.serial = serial;
        this.topicId = topicId;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_INTRODUCTION_CLAIMS;
    }

    public void submit() {
        submit(
                new LogField("username", LogFieldType.STRING, username),
                new LogField("ip_address", LogFieldType.STRING, ipAddress),
                new LogField("serial_address", LogFieldType.STRING, serial),
                new LogField("topic_id", LogFieldType.INTEGER, topicId),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
