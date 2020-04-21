package com.varrock.net.sql.batch.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.varrock.net.sql.batch.SQLTableBatchUpdateEvent;
import com.varrock.net.sql.table.impl.OutputSQLTableEntry;
import com.varrock.net.sql.transactions.SQLNetworkTransaction;

/**
 * Created by Jason MK on 2018-08-17 at 9:08 AM
 */
public class OutputBatchUpdateEvent extends SQLTableBatchUpdateEvent<OutputSQLTableEntry> {

    private static final OutputBatchUpdateEvent INSTANCE = new OutputBatchUpdateEvent();

    private OutputBatchUpdateEvent() {
        super(5);
    }

    public static OutputBatchUpdateEvent getInstance() {
        return INSTANCE;
    }

    @Override
    public void submit(OutputSQLTableEntry entry) {
        super.submit(entry);

        if (entry.getTypeToString().equals(OutputSQLTableEntry.Type.ERROR.name())) {
            forceUpdate();
        }
    }

    @Override
    protected SQLNetworkTransaction createTransaction(List<OutputSQLTableEntry> pending) {
        return connection -> {
            connection.setAutoCommit(true);
			/*try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " + SQLTable.getGameSchemaTable(SQLTable.OUTPUT_LOG) + " (time_of_output, date_and_time, content, content_type) VALUES(?, ?, ?, ?);")) {
                for (OutputSQLTableEntry entry : pending) {
                    statement.setLong(1, entry.getTimeOfOutput());
                    statement.setString(2, Misc.getDateAndTimeAndSeconds());
                    statement.setString(3, entry.getOutput());
                    statement.setString(4, entry.getTypeToString());
                    statement.addBatch();
                }
                statement.executeBatch();
            } catch (Exception e) {
			    e.printStackTrace();
            }*/
        };
    }

    @Override
    protected List<OutputSQLTableEntry> createBackingList() {
        return new CopyOnWriteArrayList<>();
    }
}
