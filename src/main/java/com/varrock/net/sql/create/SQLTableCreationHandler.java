package com.varrock.net.sql.create;

import org.apache.commons.io.FilenameUtils;

import com.varrock.net.sql.SQLNetwork;
import com.varrock.net.sql.SQLNetworkUtils;
import com.varrock.net.sql.transactions.SQLNetworkTransactionFuture;
import com.varrock.util.Misc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Created by Jason MK on 2018-08-22 at 10:17 AM
 */
public class SQLTableCreationHandler {

    private final List<SQLNetworkTransactionFuture<Boolean>> creationTransactions = new ArrayList<>();
    private final SQLNetwork network;
    private State state = State.IDLE;

    public SQLTableCreationHandler(SQLNetwork network) {
        this.network = network;
    }

    public void addAll(Path directory) {
        try (Stream<Path> stream = Files.walk(directory)) {
            stream.filter(Files::isRegularFile).forEach(this::add);
        } catch (IOException ioe) {
            throw new RuntimeException("Could not add all files in directory: " + directory, ioe);
        }
    }

    public void add(Path file) {
        if (state != State.IDLE) {
            throw new RuntimeException("Cannot add query to call if not in an idle state: " + state);
        }
        try {
            final List<String> lines = Files.readAllLines(file);

            creationTransactions.add(new SQLNetworkTransactionFuture<Boolean>() {
                @Override
                public Boolean request(Connection connection) throws SQLException {
                    final String query = String.join("", lines);

                    final String table = FilenameUtils.removeExtension(file.getFileName().toString());

                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        if (statement.executeUpdate() <= 0) {
                            try (PreparedStatement show = connection.prepareStatement("SHOW CREATE TABLE " + table + ";")) {
                                try (ResultSet results = show.executeQuery()) {
                                    if (results.next()) {
                                        String existingCreateStatement = results.getString(2);

                                        if (!SQLNetworkUtils.createMatches(existingCreateStatement, query)) {
                                            try (PreparedStatement drop = connection.prepareStatement("DROP TABLE IF EXISTS " + table + ";")) {
                                                drop.executeUpdate();

                                                try (PreparedStatement create = connection.prepareStatement(query)) {
                                                    create.executeUpdate();

                                                    return true;
                                                }
                                            }
                                        }
                                        return true;
                                    }
                                    throw new RuntimeException("Could not find create statement for table, might not exist " + table);
                                }
                            }
                        }
                        return true;
                    } catch (Exception ex) {
//                        System.out.println("failed to execute; probably exists " + table);
                    }
                    return false;
                }
            });
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to properly read all lines from file: " + file, ioe);
        }
    }

    public void updateBlocking(long time, TimeUnit unit) {
        long start = System.nanoTime();

        if (state != State.IDLE) {
            throw new RuntimeException("Cannot update while blocking if not in an idle state: " + state);
        }
        state = State.UPDATING;

        creationTransactions.forEach(network::submit);

        while (creationTransactions.stream().anyMatch(t -> !t.isFinished())) {
            try {
                unit.sleep(time);
            } catch (InterruptedException e) {
                throw new RuntimeException("Unable to update and block, thread interrupted.", e);
            }
        }

        if (creationTransactions.stream().anyMatch(SQLNetworkTransactionFuture::isCompletedWithErrors)) {
            throw new RuntimeException("A creation statement finished with errors.");
        }
        state = State.FINISHED;

        Misc.print(String.format("Successfully finished processing (%s) create statements in %s milliseconds.",
                creationTransactions.size(), TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start)));

        creationTransactions.clear();
    }

    enum State {
        IDLE,

        UPDATING,

        FINISHED
    }
}
