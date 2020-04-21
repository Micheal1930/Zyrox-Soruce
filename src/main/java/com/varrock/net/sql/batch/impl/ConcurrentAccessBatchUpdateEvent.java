package com.varrock.net.sql.batch.impl;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.varrock.net.sql.batch.SQLTableBatchUpdateEvent;
import com.varrock.net.sql.table.impl.ConcurrentAccessTableEntry;
import com.varrock.net.sql.transactions.SQLNetworkTransaction;

/**
 * Created by Jason MK on 2018-12-07 at 3:20 PM
 */
public class ConcurrentAccessBatchUpdateEvent extends SQLTableBatchUpdateEvent<ConcurrentAccessTableEntry> {

    private static final ConcurrentAccessBatchUpdateEvent instance = new ConcurrentAccessBatchUpdateEvent();

    private ConcurrentAccessBatchUpdateEvent() {
        super(1);
    }

    public static ConcurrentAccessBatchUpdateEvent getInstance() {
        return instance;
    }

    @Override
    protected List<ConcurrentAccessTableEntry> createBackingList() {
        return new CopyOnWriteArrayList<>();
    }

    @Override
    protected SQLNetworkTransaction createTransaction(List<ConcurrentAccessTableEntry> pending) {
        return connection -> {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO concurrent_access (creation_timestamp, username, method, class, thread_name, stacktrace) VALUES (?, ?, ?, ?, ?, ?);")) {
                for (ConcurrentAccessTableEntry entry : pending) {
                    statement.setString(1, entry.getTimestamp().toString());
                    statement.setString(2, entry.getUsername());
                    statement.setString(3, entry.getMethod());
                    statement.setString(4, entry.getClazz());
                    statement.setString(5, entry.getThreadName());
                    statement.setString(6, Arrays.toString(entry.getStacktrace()));
                    statement.addBatch();
                }
                statement.executeBatch();
            }
        };
    }
}
