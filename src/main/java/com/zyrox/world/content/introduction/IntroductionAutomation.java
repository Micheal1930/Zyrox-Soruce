package com.zyrox.world.content.introduction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zyrox.GameServer;
import com.zyrox.model.Item;
import com.zyrox.model.item.Items;
import com.zyrox.model.log.impl.IntroductionClaimLog;
import com.zyrox.net.sql.SQLTable;
import com.zyrox.util.Misc;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 10/4/2019
 **/
public class IntroductionAutomation {

    public static Item REWARD = new Item(Items.MYSTERY_BOX, 1);

    public static void checkForTopics(Player player) {
        if(!player.sqlCheckTimer.elapsed(10_000)) {
            DialogueManager.sendStatement(player, "@red@You can only perform a website request every 10 seconds.");
            return;
        }

        DialogueManager.sendStatement(player, "Checking for an introduction topic...");

        String introductionCheckQuery  = "SELECT * FROM "+ SQLTable.getForumSchemaTable(SQLTable.FORUMS_TOPICS)+" WHERE forum_id = 10 AND starter_name = '"+player.getName()+"'";
        GameServer.getSqlNetwork().submit(connection -> {
            try {
                try(PreparedStatement statement = connection.prepareStatement(introductionCheckQuery)) {
                    try (ResultSet results = statement.executeQuery()){

                        while (results.next()) {
                            int topicId = results.getInt("tid");
                            submitTopicClaim(player, topicId);
                            return;
                        }
                        DialogueManager.sendStatement(player, "No introduction found for '"+player.getName()+"'");
                    } catch (Exception ex) {
                        //Misc.print("No referral codes to retrieve.");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });

        player.sqlCheckTimer.reset();
    }

    public static void submitTopicClaim(Player player, int topicId) {
        String introductionCheckQuery  = "SELECT * FROM "+ SQLTable.getGameSchemaTable(SQLTable.LOGS_INTRODUCTION_CLAIMS)+" WHERE topic_id = "+topicId+" OR ip_address = '"+player.getHostAddress()+"' OR serial_address = '"+player.getSuperSerialNumber()+"'";
        GameServer.getSqlNetwork().submit(connection -> {
            try {
                try(PreparedStatement statement = connection.prepareStatement(introductionCheckQuery)) {
                    try (ResultSet results = statement.executeQuery()){

                        boolean foundResults = false;

                        while (results.next()) {
                            String serial = results.getString("serial_address");

                            if (serial == null || serial.equalsIgnoreCase("null") || serial.isEmpty()) {
                                continue;
                            }

                            foundResults = true;

                        }

                        if(!foundResults) {
                            DialogueManager.start(player, new Dialogue() {
                                @Override
                                public DialogueType type() {
                                    return DialogueType.ITEM_STATEMENT;
                                }

                                @Override
                                public String[] item() {
                                    return new String[]{
                                            String.valueOf(REWARD.getId()),
                                            "180",
                                            REWARD.getDefinition().getName()
                                    };
                                }

                                @Override
                                public String[] dialogue() {
                                    return new String[]{
                                            "You received a reward for making an introduction",
                                    };
                                }
                            });

                            player.getInventory().add(REWARD);

                            new IntroductionClaimLog(player.getName(), player.getHostAddress(), player.getSuperSerialNumber(), topicId, Misc.getTime()).submit();
                        } else {
                            DialogueManager.sendStatement(player, "You have already claimed an introduction.");
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        //Misc.print("No referral codes to retrieve.");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });
    }

}
