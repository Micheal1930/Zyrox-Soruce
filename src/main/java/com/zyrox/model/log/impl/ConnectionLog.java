package com.zyrox.model.log.impl;

import com.zyrox.model.log.Log;
import com.zyrox.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class ConnectionLog extends Log {

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_CONNECTIONS;
    }

}
