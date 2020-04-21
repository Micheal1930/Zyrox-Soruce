package com.varrock.model.log.impl;

import com.varrock.model.log.Log;
import com.varrock.model.log.LogField;
import com.varrock.model.log.LogFieldType;
import com.varrock.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class GlobalStatisticLog extends Log {

    private final int playersOnline;
    private final int staffOnline;
    private final int wildernessCount;
    private final String time;

    public GlobalStatisticLog(int playersOnline, int staffOnline, int wildernessCount, String time) {
        this.playersOnline = playersOnline;
        this.staffOnline = staffOnline;
        this.wildernessCount = wildernessCount;
        this.time = time;
    }

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_GLOBAL_STATISTICS;
    }

    public void submit() {
        submit(
                new LogField("players_online", LogFieldType.STRING, playersOnline),
                new LogField("staff_online", LogFieldType.STRING, staffOnline),
                new LogField("wilderness_count", LogFieldType.STRING, wildernessCount),
                new LogField("time", LogFieldType.STRING, time)
        );
    }

}
