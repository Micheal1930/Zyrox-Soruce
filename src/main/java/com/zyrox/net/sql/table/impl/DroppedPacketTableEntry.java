package com.zyrox.net.sql.table.impl;

import java.time.LocalDateTime;

import com.zyrox.net.sql.table.SQLTableEntry;

/**
 * Created by Jason MK on 2018-12-05 at 3:39 PM
 */
public class DroppedPacketTableEntry implements SQLTableEntry {

    private final int opcode;

    private final int read;

    private final int remaining;

    private final LocalDateTime timestamp;

    private final String username;

    private final String ip;

    private final int clientVersion;

    private final String coords;

    public DroppedPacketTableEntry(int opcode, int read, int remaining, String username, String ip, int clientVersion, String coords) {
        this.opcode = opcode;
        this.read = read;
        this.remaining = remaining;
        this.username = username;
        this.ip = ip;
        this.clientVersion = clientVersion;
        this.timestamp = LocalDateTime.now();
        this.coords = coords;
    }

    public int getOpcode() {
        return opcode;
    }

    public int getRead() {
        return read;
    }

    public int getRemaining() {
        return remaining;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
    }

    public int getClientVersion() {
        return clientVersion;
    }

    public String getCoords() {
        return coords;
    }
}
