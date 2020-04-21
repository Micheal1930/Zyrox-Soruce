package com.varrock.world.content.social;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.varrock.GameServer;
import com.varrock.model.Item;
import com.varrock.model.input.Input;
import com.varrock.model.log.impl.SocialCodeLog;
import com.varrock.net.sql.SQLTable;
import com.varrock.util.Misc;
import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 10/21/2019
 **/
public class SocialCodeManager {

    public static Map<String, SocialCode> SOCIAL_CODES = new HashMap<>();

    public static void openSocial(Player player) {

        boolean nulledSerial = player.getSuperSerialNumber() == null || player.getSuperSerialNumber().equalsIgnoreCase("null") || player.getSuperSerialNumber().isEmpty();


        player.setInputHandling(new Input() {
            @Override
            public void handleSyntax(Player player, String text) {
                if(!SOCIAL_CODES.containsKey(text.toLowerCase())) {
                    DialogueManager.sendStatement(player, "This social code is invalid.");
                    return;
                }
                SocialCode code = SOCIAL_CODES.get(text.toLowerCase());

                if (code.getUsedUsernames().contains(player.getName())
                        || code.getUsedIpAddresses().contains(player.getHostAddress())
                        || (code.getUsedSerialAddresses().contains(player.getSuperSerialNumber()) && !nulledSerial)) {
                    DialogueManager.sendStatement(player, "You can only use this code once.");
                    return;
                }

                if(code.getUsedUsernames().size() >= code.getUsesAllowed()) {
                    DialogueManager.sendStatement(player, "This code has been used "+code.getUsesAllowed()+" times already.");
                    return;
                }

                player.getInventory().add(code.getReward());

                DialogueManager.start(player, new Dialogue() {
                    @Override
                    public DialogueType type() {
                        return DialogueType.ITEM_STATEMENT;
                    }

                    @Override
                    public String[] item() {
                        return new String[]{
                                String.valueOf(code.getReward().getId()),
                                "180",
                                code.getReward().getDefinition().getName()
                        };
                    }

                    @Override
                    public String[] dialogue() {
                        return new String[]{
                                "You received a reward for using a social code.",
                        };
                    }
                });
                code.getUsedUsernames().add(player.getName().toLowerCase());
                code.getUsedIpAddresses().add(player.getHostAddress());
                code.getUsedSerialAddresses().add(player.getSuperSerialNumber());
                new SocialCodeLog(player.getName(), text.toLowerCase(), player.getHostAddress(), player.getSuperSerialNumber(), Misc.getTime()).submit();
            }
        });
        player.getPacketSender().sendEnterInputPrompt("Enter social code:");
    }

    public static void load() {
        SOCIAL_CODES.clear();
        String socialCodeQuery = "SELECT * FROM "+ SQLTable.getGameSchemaTable(SQLTable.DATA_SOCIAL_CODES)+" ORDER BY id";
        GameServer.getSqlNetwork().submit(connection -> {
            try {
                try(PreparedStatement statement = connection.prepareStatement(socialCodeQuery)) {
                    try (ResultSet results = statement.executeQuery()){

                        while (results.next()) {

                            String socialCode = results.getString("social_code");
                            int itemId = results.getInt("item_id");
                            int amount = results.getInt("amount");
                            int uses = results.getInt("uses");

                            SOCIAL_CODES.put(socialCode.toLowerCase(), new SocialCode(new Item(itemId, amount), uses));

                        }
                    } catch (Exception ex) {
                        Misc.print("No social codes to retrieve.");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });

        String socialLogQuery  = "SELECT * FROM "+ SQLTable.getGameSchemaTable(SQLTable.LOGS_SOCIAL_CODES)+" ORDER BY id";
        GameServer.getSqlNetwork().submit(connection -> {
            try {
                try(PreparedStatement statement = connection.prepareStatement(socialLogQuery)) {
                    try (ResultSet results = statement.executeQuery()){

                        while (results.next()) {

                            String username = results.getString("username");
                            String ipAddress = results.getString("ip_address");
                            String serial = results.getString("serial");
                            String code = results.getString("code");

                            if(SOCIAL_CODES.containsKey(code.toLowerCase())) {
                                SOCIAL_CODES.get(code.toLowerCase()).getUsedUsernames().add(username.toLowerCase());
                                SOCIAL_CODES.get(code.toLowerCase()).getUsedIpAddresses().add(ipAddress);
                                SOCIAL_CODES.get(code.toLowerCase()).getUsedSerialAddresses().add(serial);
                            }

                        }
                    } catch (Exception ex) {
                        Misc.print("No social codes to retrieve.");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });
    }
}
