package com.zyrox.net.sql;

import com.zyrox.net.sql.transactions.SQLNetworkTransaction;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Created by Jason MacKeigan on 2018-06-16 at 3:22 PM
 * <p>
 * A network that doesn't actually submit transactions or process them, etc. It simply acts
 * as a mock of a sql network.
 */
public class MockSQLNetwork extends SQLNetwork {

    public MockSQLNetwork(HikariConfig config) {
        super(config, -1, null, -1);
    }

    @Override
    public HikariDataSource createDataSource(HikariConfig config) {
        return new HikariDataSource();
    }

    @Override
    public void blockingTest() {

    }

    @Override
    public void start() {

    }

    @Override
    public void submit(SQLNetworkTransaction transaction) {

    }

    @Override
    public void run() {

    }

}
