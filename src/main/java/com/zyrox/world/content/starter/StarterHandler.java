package com.zyrox.world.content.starter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.zyrox.GameServer;
import com.zyrox.net.sql.SQLNetwork;
import com.zyrox.net.sql.SQLTable;
import com.zyrox.net.sql.query.impl.StringParameter;
import com.zyrox.util.Misc;

/**
 * Created by Jonny on 8/2/2019
 **/
public class StarterHandler {

    public HashMap<String, Integer> STARTER_SERIALS = new HashMap<>();

    public HashMap<String, Integer> STARTER_ADDRESSES = new HashMap<>();

    public void addStarter(String ipAddress, String jserial) {
    	if(ipAddress != null)
    		return;
        SQLNetwork.insert("INSERT INTO " + SQLTable.getGameSchemaTable(SQLTable.SAVES_STARTERS) + " (ip_address, jserial) VALUES(?, ?)", new StringParameter(1, ipAddress), new StringParameter(2, jserial));
        STARTER_SERIALS.merge(jserial, 1, (a, b) -> a + b);
        STARTER_ADDRESSES.merge(ipAddress, 1, (a, b) -> a + b);
    }

    public boolean hasSerialStarter(String jserial) {
        if(jserial == null || jserial.equalsIgnoreCase("null") || jserial.isEmpty())
            return true;

        int total = STARTER_SERIALS.get(jserial) != null ? STARTER_SERIALS.get(jserial) : 0;
        return total <= 2;
    }

    public boolean hasIpStarter(String ipAddress) {
        int total = STARTER_ADDRESSES.get(ipAddress) != null ? STARTER_ADDRESSES.get(ipAddress) : 0;
        return total <= 2;
    }

    public boolean hasAnyStarter(String ipAddress, String serial) {
        return hasIpStarter(ipAddress) || hasSerialStarter(serial);
    }

    public void load() {
        String query = "SELECT * FROM "+ SQLTable.getGameSchemaTable(SQLTable.SAVES_STARTERS)+" ORDER BY id";
        GameServer.getSqlNetwork().submit(connection -> {
            try {
                try(PreparedStatement statement = connection.prepareStatement(query)) {
                    try (ResultSet results = statement.executeQuery()){

                        while (results.next()) {
                            String ipAddress = results.getString("ip_address");
                            String jserial = results.getString("jserial");
                            STARTER_ADDRESSES.merge(ipAddress, 1, (a, b) -> a + b);
                            STARTER_SERIALS.merge(jserial, 1, (a, b) -> a + b);
                        }
                    } catch (Exception ex) {
                        Misc.print("No starter information to retrieve.");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });
    }//just sql
}
