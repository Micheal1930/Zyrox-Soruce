package com.varrock.model.log.impl;

import com.varrock.model.log.Log;
import com.varrock.net.sql.SQLTable;

/**
 * Created by Jonny on 7/25/2019
 **/
public class ConnectionLog extends Log {

    @Override
    public SQLTable getSqlTable() {
        return SQLTable.LOGS_CONNECTIONS;
    }

}
