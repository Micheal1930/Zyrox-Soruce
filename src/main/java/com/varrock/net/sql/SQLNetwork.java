package com.varrock.net.sql;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.varrock.GameServer;
import com.varrock.GameSettings;
import com.varrock.net.sql.query.Parameter;
import com.varrock.net.sql.transactions.SQLNetworkTransaction;
import com.varrock.util.Misc;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by Jason MacKeigan on 2018-06-15 at 10:04 AM
 */
public class SQLNetwork implements Runnable {

    private final HikariConfig config;

    private final long cyclePeriod;

    private final TimeUnit cycleUnit;

    private final int transactionsPerCycle;

    private final Queue<SQLNetworkTransaction> transactions = new ConcurrentLinkedQueue<>();

    private final ScheduledExecutorService networkService = Executors.newSingleThreadScheduledExecutor(
            new ThreadFactoryBuilder().setNameFormat("SQL-Network").setUncaughtExceptionHandler((thread, throwable) -> {
                Misc.print(throwable);
            }).build());

    private final HikariDataSource dataSource;

    private ScheduledFuture<?> task;

    public SQLNetwork(HikariConfig config, long cyclePeriod, TimeUnit cycleUnit, int transactionsPerCycle) {
        this.config = config;
        this.cyclePeriod = cyclePeriod;
        this.cycleUnit = cycleUnit;
        this.transactionsPerCycle = transactionsPerCycle;
        this.dataSource = createDataSource(config);
    }

    public static void insert(String query, Parameter... parameters) {
        if (GameSettings.MOCK_SQL) {
            return;
        }
        GameServer.getSqlNetwork().submitUpdate(query, parameters);
    }

    public HikariDataSource createDataSource(HikariConfig config) {
        config.setAutoCommit(true);
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(25);
        config.setConnectionTimeout(1_000);
        config.setIdleTimeout(10_000);
        config.setScheduledExecutor(networkService);
        config.setConnectionTestQuery("SELECT 1");

        return new HikariDataSource(config);
    }

    public void blockingTest() {
        try (Connection connection = createConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(config.getConnectionTestQuery())) {
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not create connection for test.", e);
        }
    }

    public void start() {
        if (task != null) {
            throw new RuntimeException("Already started.");
        }
        reschedule(cycleUnit, cyclePeriod);
    }

    public void reschedule() {
        if (task == null) {
            throw new RuntimeException("Not yet started.");
        }
        reschedule(cycleUnit, cyclePeriod);
    }

    public void reschedule(TimeUnit unit, long period) {
        if (task != null) {
            task.cancel(true);
        }
        task = networkService.scheduleAtFixedRate(this, 0, period, unit);
    }

    public void submit(SQLNetworkTransaction transaction) {
        if (GameSettings.MOCK_SQL) {
            return;
        }
        transactions.add(transaction);
    }

    private void submitUpdate(String query, Parameter... parameters) {
        if (GameSettings.MOCK_SQL) {
            return;
        }
        submit(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                for (Parameter parameter : parameters) {
                    parameter.set(statement);
                }
                statement.executeUpdate();
            }
        });
    }

    public void shutdownBlocking() {
        try {
            if (!networkService.awaitTermination(1, TimeUnit.SECONDS)) {
                networkService.shutdownNow();
            }
        } catch (InterruptedException e) {
            networkService.shutdownNow();

            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        try {
            if (transactions.isEmpty()) {
                return;
            }
            long start = System.nanoTime();

            try (Connection connection = createConnection()) {
                int transactionsExecuted = 0;

                SQLNetworkTransaction transaction;

                while ((transaction = transactions.poll()) != null) {
                    transaction.call(connection);

                    if (++transactionsExecuted >= transactionsPerCycle) {
                        break;
                    }
                    long elapsed = System.nanoTime() - start;

                    if (elapsed >= cycleUnit.toNanos(cyclePeriod)) {
                        Misc.printWarning("SQLNetwork transactions are taking longer than expected...");
                        break;
                    }
                }
            } catch (SQLException exception) {
                Misc.printWarning("Unable to create connection.");
                Misc.print(exception);
            }
        } catch (Exception e) {
            Misc.printWarning("Exception occurred on sql-network that may have caused it to shut down.");
            Misc.print(e);
        }
    }

    private Connection createConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
