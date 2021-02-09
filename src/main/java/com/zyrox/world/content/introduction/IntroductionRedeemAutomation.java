package com.zyrox.world.content.introduction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zyrox.GameServer;
import com.zyrox.model.Item;
import com.zyrox.model.item.Items;
import com.zyrox.net.sql.SQLTable;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 10/4/2019
 **/
public class IntroductionRedeemAutomation {

    public static Item REWARD = new Item(Items.MYSTERY_BOX, 1);

    public static void checkForRedeem(Player player) {
        if(!player.sqlCheckTimer.elapsed(10_000)) {
            DialogueManager.sendStatement(player, "@red@You can only perform a website request every 10 seconds.");
            return;
        }

        DialogueManager.sendStatement(player, "Checking for an introduction in #redeem...");

        String introductionCheckQuery  = "SELECT * FROM "+ SQLTable.getGameSchemaTable(SQLTable.SAVES_REDEEMS)+" WHERE username = '"+player.getName()+"'";
        GameServer.getSqlNetwork().submit(connection -> {
            try {
                try(PreparedStatement statement = connection.prepareStatement(introductionCheckQuery)) {
                    try (ResultSet results = statement.executeQuery()){

                        while (results.next()) {
                            int id = results.getInt("id");
                            submitRedeemClaim(player, id);
                            return;
                        }
                        DialogueManager.sendStatement(player, "No redeem found for '"+player.getName()+"'");
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

    public static void submitRedeemClaim(Player player, int id) {
        String query = "DELETE FROM "+ SQLTable.getGameSchemaTable(SQLTable.SAVES_REDEEMS)+" WHERE id = '"+id+"'";
        GameServer.getSqlNetwork().submit(connection -> {
            try {
                try(PreparedStatement statement = connection.prepareStatement(query)) {
                    int status = statement.executeUpdate();

                    if(status == 0) {
                        return;
                    }

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
                                    "You received a reward for posting in #redeem",
                            };
                        }
                    });

                    player.getInventory().add(REWARD);

                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });
    }

}
