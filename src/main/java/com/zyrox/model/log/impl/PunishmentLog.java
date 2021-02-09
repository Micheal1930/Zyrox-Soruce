package com.zyrox.model.log.impl;

import com.zyrox.model.log.Log;
import com.zyrox.model.log.LogField;
import com.zyrox.model.log.LogFieldType;
import com.zyrox.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class PunishmentLog extends Log {

    private final String punisher;
    private final String target;
    private final String type;
    private final String duration;
    private final String time;

    public PunishmentLog(String punisher, String target, String type, String duration, String time) {
        this.punisher = punisher;
        this.target = target;
        this.type = type;
        this.duration = duration;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_PUNISHMENTS;
    }

    public void submit() {
        submit(
                new LogField("punisher", LogFieldType.STRING, punisher),
                new LogField("target", LogFieldType.STRING, target),
                new LogField("type", LogFieldType.STRING, type),
                new LogField("duration", LogFieldType.STRING, duration),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
