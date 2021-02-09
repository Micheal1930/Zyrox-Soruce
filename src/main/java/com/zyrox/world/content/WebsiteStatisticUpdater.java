package com.zyrox.world.content;

import java.sql.PreparedStatement;

import com.zyrox.GameServer;
import com.zyrox.net.sql.SQLTable;
import com.zyrox.world.World;

/**
 * Created by Jonny on 9/29/2019
 **/
public class WebsiteStatisticUpdater {

    public static void updatePlayersOnline() {
        GameServer.getSqlNetwork().submit(connection -> {
            String query = "INSERT INTO "+ SQLTable.getGameSchemaTable(SQLTable.STATISTICS_ONLINE)+" (online, wilderness) VALUES (?, ?) ON DUPLICATE KEY UPDATE online=?, wilderness=?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                //insert
                statement.setInt(1, World.getPlayersOnline());
                statement.setInt(2, World.getWildernessCount());

                //update
                statement.setInt(3, World.getPlayersOnline());
                statement.setInt(4, World.getWildernessCount());

                statement.executeUpdate();
            }
        });
    }
}
