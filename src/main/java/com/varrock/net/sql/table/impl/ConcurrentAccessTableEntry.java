package com.varrock.net.sql.table.impl;

import java.time.LocalDateTime;

import com.varrock.net.sql.table.SQLTableEntry;

/**
 * Created by Jason MK on 2018-12-07 at 3:15 PM
 */
public class ConcurrentAccessTableEntry implements SQLTableEntry {

    private final String username;

    private final LocalDateTime timestamp;

    private final String method;

    private final String clazz;

    private final String threadName;

    private final String[] stacktrace;

    public ConcurrentAccessTableEntry(String username, LocalDateTime timestamp, String method, String clazz, String threadName, String[] stacktrace) {
        this.username = username;
        this.timestamp = timestamp;
        this.method = method;
        this.clazz = clazz;
        this.threadName = threadName;
        this.stacktrace = stacktrace;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMethod() {
        return method;
    }

    public String getClazz() {
        return clazz;
    }

    public String getThreadName() {
        return threadName;
    }

    public String[] getStacktrace() {
        return stacktrace;
    }
}
