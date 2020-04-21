package com.varrock.world.content;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.varrock.GameServer;
import com.varrock.model.Item;
import com.varrock.model.input.Input;
import com.varrock.model.log.impl.ReferralCodeLog;
import com.varrock.net.sql.SQLTable;
import com.varrock.util.Misc;
import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 10/1/2019
 **/
public class ReferralCode {

    public static Map<String, Item> REFERRAL_CODES = new HashMap<>();

    public static ArrayList<String> USED_REFERRALS_USERNAMES = new ArrayList<>();
    public static ArrayList<String> USED_REFERRALS_IPS = new ArrayList<>();
    public static ArrayList<String> USED_REFERRALS_SERIALS = new ArrayList<>();

    public static void openReferral(Player player, boolean tutorial) {

        if (player.newPlayer()) {
            player.setPlayerLocked(false);
            player.setNewPlayer(false);
            player.save();
        }

        boolean nulledSerial = player.getSuperSerialNumber() == null || player.getSuperSerialNumber().equalsIgnoreCase("null") || player.getSuperSerialNumber().isEmpty();

        if (USED_REFERRALS_USERNAMES.contains(player.getName())
                || USED_REFERRALS_IPS.contains(player.getHostAddress())
                || (USED_REFERRALS_SERIALS.contains(player.getSuperSerialNumber()) && !nulledSerial)) {
            if(!tutorial)
                player.sendMessage("You can only use one referral code.");
            return;
        }

        player.setInputHandling(new Input() {
            @Override
            public void handleSyntax(Player player, String text) {
                if(!REFERRAL_CODES.containsKey(text.toLowerCase())) {
                    DialogueManager.sendStatement(player, "This referral code is invalid.");
                    return;
                }
                Item item = REFERRAL_CODES.get(text.toLowerCase());

                player.getInventory().add(item);

                DialogueManager.start(player, new Dialogue() {
                    @Override
                    public DialogueType type() {
                        return DialogueType.ITEM_STATEMENT;
                    }

                    @Override
                    public String[] item() {
                        return new String[]{
                                String.valueOf(item.getId()),
                                "180",
                                item.getDefinition().getName()
                        };
                    }

                    @Override
                    public String[] dialogue() {
                        return new String[]{
                                "You received a reward for using a referral code.",
                        };
                    }
                });
                USED_REFERRALS_USERNAMES.add(player.getName().toLowerCase());
                USED_REFERRALS_IPS.add(player.getHostAddress());
                USED_REFERRALS_SERIALS.add(player.getSuperSerialNumber());
                new ReferralCodeLog(player.getName(), text.toLowerCase(), player.getHostAddress(), player.getSuperSerialNumber(), Misc.getTime()).submit();
            }
        });
        player.getPacketSender().sendEnterInputPrompt("Enter referral code:");
    }

    public static void load() {
        REFERRAL_CODES.clear();
        USED_REFERRALS_SERIALS.clear();
        USED_REFERRALS_IPS.clear();
        String referralDataQuery = "SELECT * FROM "+ SQLTable.getGameSchemaTable(SQLTable.DATA_REFERRAL_CODES)+" ORDER BY id";
        GameServer.getSqlNetwork().submit(connection -> {
            try {
                try(PreparedStatement statement = connection.prepareStatement(referralDataQuery)) {
                    try (ResultSet results = statement.executeQuery()){

                        while (results.next()) {

                            String referralCode = results.getString("referral_code");
                            int itemId = results.getInt("item_id");
                            int amount = results.getInt("amount");

                            REFERRAL_CODES.put(referralCode.toLowerCase(), new Item(itemId, amount));

                        }
                    } catch (Exception ex) {
                        Misc.print("No referral codes to retrieve.");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });

        String referralLogQuery  = "SELECT * FROM "+ SQLTable.getGameSchemaTable(SQLTable.LOGS_REFERRAL_CODES)+" ORDER BY id";
        GameServer.getSqlNetwork().submit(connection -> {
            try {
                try(PreparedStatement statement = connection.prepareStatement(referralLogQuery)) {
                    try (ResultSet results = statement.executeQuery()){

                        while (results.next()) {

                            String username = results.getString("username");
                            String ipAddress = results.getString("ip_address");
                            String serial = results.getString("serial");

                            USED_REFERRALS_USERNAMES.add(username.toLowerCase());
                            USED_REFERRALS_IPS.add(ipAddress);
                            USED_REFERRALS_SERIALS.add(serial);

                        }
                    } catch (Exception ex) {
                        Misc.print("No referral codes to retrieve.");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });
    }

}
