package com.varrock.model.punish;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.varrock.GameServer;
import com.varrock.model.OfflineCharacter;
import com.varrock.model.log.impl.PunishmentLog;
import com.varrock.net.sql.SQLTable;
import com.varrock.util.Misc;
import com.varrock.util.Stopwatch;
import com.varrock.world.World;
import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.entity.impl.player.Player;
import com.varrock.world.entity.impl.player.PlayerHandler;

/**
 * Created by Jonny on 8/12/2019
 **/
public class PunishmentManager {

    public boolean isPunished(String data, PunishmentType type) {
        data = data.toLowerCase();

        if(type == PunishmentType.J_BANNED || type == PunishmentType.J_MUTED) {
            if(data == null)
                return false;

            if(data.equalsIgnoreCase("null"))
                return false;

            if(data.isEmpty())
                return false;
        }

        if(!type.punishments.containsKey(data)) {
            return false;
        }

        Punishment punishment = type.punishments.get(data);

        if(punishment.type == type) {
            if(!punishment.timer.elapsed(punishment.duration)) {
                return true;
            }
        }

        return false;
    }

    public Punishment addPunishment(Player other, PunishmentType type, String data, long duration, boolean insert) {
        data = data.toLowerCase();

        if(isPunished(data, type)) {
            return null;
        }

        if(data == null || data.isEmpty()) {
            return null;
        }

        Punishment punishment = new Punishment(data, type, duration);

        type.punishments.put(data, punishment);

        if(type.needsKicked) {
            if(other != null) {
                World.deregister(other);
                PlayerHandler.handleLogout(other);
            }
        }

        if(other != null) {
            other.sendErrorMessage("You have been "+type.getName()+" for "+ Misc.getTimeLeftForTimer(duration, new Stopwatch().reset()));
        }

        if(insert) {

            String finalData = data;
            GameServer.getSqlNetwork().submit(connection -> {
                String query = "INSERT INTO "+ SQLTable.getGameSchemaTable(SQLTable.SAVES_PUNISHMENTS)+" (punishment_type, punishment_data, timer, duration) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE punishment_type=?, punishment_data=?, timer=?, duration=?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    //insert
                    statement.setString(1, type.toString());
                    statement.setString(2, finalData);
                    statement.setLong(3, punishment.timer.getTime());
                    statement.setLong(4, duration);

                    //update
                    statement.setString(5, type.toString());
                    statement.setString(6, finalData);
                    statement.setLong(7, punishment.timer.getTime());
                    statement.setLong(8, duration);

                    statement.executeUpdate();
                }
            });
        }
        return punishment;
    }

    public boolean removePunishment(Player player, String data, PunishmentType type) {

        if(data != null) {
            data = data.toLowerCase();
        }

        if(!type.punishments.containsKey(data)) {
            return false;
        }

        if(data == null || data.isEmpty()) {
            return false;
        }

        String query = "DELETE FROM "+ SQLTable.getGameSchemaTable(SQLTable.SAVES_PUNISHMENTS)+" WHERE punishment_data = '"+data+"' AND punishment_type = '"+type.toString()+"'";
        GameServer.getSqlNetwork().submit(connection -> {
            try {
                try(PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.executeUpdate();
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });

        Punishment punishment = type.punishments.get(data);

        if(punishment != null) {
            type.punishments.remove(data);
        }

        new PunishmentLog(player.getName(), data, "REMOVE_"+type.toString(), "unavailable", Misc.getTime()).submit();

        return true;
    }

    public void startPunishment(Player player, String username, String data, PunishmentType punishmentType) {
        data = data.toLowerCase();

        String finalData = data;
        DialogueManager.start(player, new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.OPTION;
            }

            @Override
            public String[] dialogue() {
                return new String[] {
                        "1 day",
                        "1 week",
                        "1 month",
                        "10 year"
                };
            }

            @Override
            public boolean action(int option) {

                player.getPA().closeDialogueOnly();

                long duration = 0;

                switch(option) {
                    case 1:
                        duration = 86400000L; // 1 day
                        break;
                    case 2:
                        duration = 86400000L * 7L; //1 week
                        break;
                    case 3:
                        duration = 86400000L * 30L; //1 month
                        break;
                    case 4:
                        duration = 86400000L * 365L * 10L; //10 year
                        break;
                }

                if(duration > 0) {
                    Player other = World.getPlayerByName(username);

                    Punishment punishment = addPunishment(other, punishmentType, finalData, duration, true);

                    new PunishmentLog(player.getName(), username, punishmentType.getName(), Misc.getTimeLeftForTimer(duration, new Stopwatch().reset()), Misc.getTime()).submit();

                    if(punishment == null) {
                        player.sendMessage("This player already has an active punishment. Remove their punishment and try again.");
                    } else {
                        player.sendMessage("<col=ff0000>You have "+punishmentType.getName()+" "+username+" for "+ Misc.getTimeLeftForTimer(duration, new Stopwatch().reset()));
                    }
                }
                return true;
            }
        });
    }

    public void load() {
        String query = "SELECT * FROM "+ SQLTable.getGameSchemaTable(SQLTable.SAVES_PUNISHMENTS)+" ORDER BY id";
        GameServer.getSqlNetwork().submit(connection -> {
            try {
                try(PreparedStatement statement = connection.prepareStatement(query)) {
                    try (ResultSet results = statement.executeQuery()){

                        while (results.next()) {
                            PunishmentType punishmentType = PunishmentType.valueOf(results.getString("punishment_type"));
                            String data = results.getString("punishment_data");
                            long timerStart = results.getLong("timer");
                            long duration = results.getLong("duration");

                            Punishment punishment = addPunishment(null, punishmentType, data, duration, false);
                            punishment.timer.setTime(timerStart);

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Misc.print("No punishment information to retrieve.");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });
    }

    public void checkPunishments(Player player, String username) {
        boolean foundPunishment = false;

        Player other = OfflineCharacter.getOfflineCharacter(username);

        if(other == null) {
            DialogueManager.sendStatement(player, "@red@"+username+" does not exist.");
            return;
        }

        for(PunishmentType punishmentType : PunishmentType.values()) {
            if(punishmentType == PunishmentType.GAMBLE_BAN)
                continue;

            String data = punishmentType.getDataForPlayer(other).toLowerCase();

            if(isPunished(data, punishmentType)) {

                Punishment punishment = punishmentType.punishments.get(data);

                player.sendMessage("<col=ff0000>"+Misc.capitalize(username)+" is "+punishmentType.getName()+" for another "+Misc.getTimeLeftForTimer(punishment.duration, punishment.timer)+".");

                foundPunishment = true;
            }
        }

        if(!foundPunishment) {
            player.sendMessage(username+" does not have any active punishments.");
        }
    }

    public boolean isMuted(Player player) {
        if (isPunished(player.getName().toLowerCase(), PunishmentType.MUTED)) {
            checkPunishments(player, player.getName().toLowerCase());
            return true;
        }
        if (isPunished(player.getName().toLowerCase(), PunishmentType.J_MUTED)) {
            checkPunishments(player, player.getName().toLowerCase());
            return true;
        }
        return false;
    }

}
