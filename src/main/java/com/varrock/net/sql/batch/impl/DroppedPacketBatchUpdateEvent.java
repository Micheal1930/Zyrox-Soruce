package com.varrock.net.sql.batch.impl;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import com.varrock.net.sql.batch.SQLTableBatchUpdateEvent;
import com.varrock.net.sql.table.impl.DroppedPacketTableEntry;
import com.varrock.net.sql.transactions.SQLNetworkTransaction;

/**
 * Created by Jason MK on 2018-12-05 at 3:39 PM
 */
public class DroppedPacketBatchUpdateEvent extends SQLTableBatchUpdateEvent<DroppedPacketTableEntry> {

    private static final DroppedPacketBatchUpdateEvent instance = new DroppedPacketBatchUpdateEvent();

    private DroppedPacketBatchUpdateEvent() {
        super(10);
    }

    public static DroppedPacketBatchUpdateEvent getInstance() {
        return instance;
    }

    @Override
    protected List<DroppedPacketTableEntry> createBackingList() {
        return new ArrayList<>();
    }

    @Override
    protected SQLNetworkTransaction createTransaction(List<DroppedPacketTableEntry> pending) {
        return connection -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO dropped_packets (datetime, opcode, bytes_read, bytes_remaining, username, ip, client_version, coords) VALUES(?, ?, ?, ?, ?, ?, ?, ?);")) {
                for (DroppedPacketTableEntry entry : pending) {
                    statement.setString(1, entry.getTimestamp().toString());
                    statement.setInt(2, entry.getOpcode());
                    statement.setInt(3, entry.getRead());
                    statement.setInt(4, entry.getRemaining());
                    statement.setString(5, entry.getUsername());
                    statement.setString(6, entry.getIp());
                    statement.setInt(7, entry.getClientVersion());
                    statement.setString(8, entry.getCoords());
                    statement.addBatch();
                }
                statement.executeBatch();
            }
        };
    }
}
