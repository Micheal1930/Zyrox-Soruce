package com.zyrox.net.sql.batch.impl;

import java.util.ArrayList;
import java.util.List;

import com.zyrox.net.sql.batch.SQLTableBatchUpdateEvent;
import com.zyrox.net.sql.table.impl.LongStringSQLTableEntry;
import com.zyrox.net.sql.transactions.SQLNetworkTransaction;

/**
 * Created by Jason MK on 2018-08-13 at 10:54 AM
 */
public class LongStringBatchUpdateEvent extends SQLTableBatchUpdateEvent<LongStringSQLTableEntry> {

    private static final LongStringBatchUpdateEvent INSTANCE = new LongStringBatchUpdateEvent();

    public LongStringBatchUpdateEvent() {
        super(1);
    }

    public static LongStringBatchUpdateEvent getInstance() {
        return INSTANCE;
    }

    @Override
    protected SQLNetworkTransaction createTransaction(List<LongStringSQLTableEntry> pending) {
        return connection -> {
           /* try (PreparedStatement statement = connection.prepareStatement(String.format("INSERT INTO %s (length, content) VALUES(?, ?);",
                    SQLTable.LOGS_LONG_STRING.toTableName()))) {
                for (LongStringSQLTableEntry entry : pending) {
                    statement.setInt(1, entry.getLength());
                    statement.setString(2, entry.getContent());
                    statement.addBatch();
                }
                statement.executeBatch();
            }*/
        };
    }

    @Override
    protected List<LongStringSQLTableEntry> createBackingList() {
        return new ArrayList<>();
    }
}
