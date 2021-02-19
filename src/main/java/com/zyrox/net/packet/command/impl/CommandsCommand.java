package com.zyrox.net.packet.command.impl;

import java.util.*;

import com.zyrox.model.Item;
import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.net.packet.impl.CommandPacketListener;
import com.zyrox.world.entity.impl.npc.DropViewer;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = {"commands"}, description = "Displays a list of available commands to you.")
public class CommandsCommand extends Command {

    @Override
    public boolean execute(Player player, String[] args) throws Exception {

        player.getPacketSender().sendItemsOnInterface(58800, 200, new ArrayList<Item>(), true);

        player.getPacketSender().sendString(8134, "");

        List<String> commands = new ArrayList<>();

        if (args.length == 0) {
            CommandPacketListener.getCommands().forEach((key, command) -> {

                if (command instanceof NameCommand) {
                    NameCommand nameCommand = (NameCommand) command;

                    if (nameCommand.getValidNames().contains(player.getUsername())) {
                        commands.add("@whi@::" + key);
                        if(nameCommand.getHeader() == null) {
                            commands.add("No description available");
                        } else {
                            commands.add("@or2@" + nameCommand.getHeader().description());
                        }
                    }
                } else {
                    if (player.getRights().getPrivilegeLevel() >= command.getMininumStaffPrivilege() && player.getRights().getDonatorPrivilegelLevel() >= command.getMinimumDonatorPrivilege()) {
                        commands.add("@whi@::" + key);
                        if(command.getHeader() == null) {
                            commands.add("@whi@No description available");
                        } else {
                            commands.add("@or2@" + command.getHeader().description());
                        }
                    }
                }

            });
        } else {
            PlayerRights rights = PlayerRights.valueOf(String.join("_", args).toUpperCase());

            if (rights == null || (rights.getPrivilegeLevel() > player.getRights().getPrivilegeLevel() && rights.getDonatorPrivilegelLevel() > player.getRights().getDonatorPrivilegelLevel())) {
                return false;
            }

            CommandPacketListener.getCommands().forEach((key, command) -> {
                if (rights.getPrivilegeLevel() <= command.getMininumStaffPrivilege() && rights.getDonatorPrivilegelLevel() <= command.getMinimumDonatorPrivilege()) {
                    commands.add("@whi@::" + key);
                    if(command.getHeader() == null) {
                        commands.add("@whi@No description available");
                    } else {
                        commands.add("@or2@" + command.getHeader().description());
                    }
                }
            });

            player.sendMessage("Showing commands for @dre@" + rights.toString() + "");
        }

        DropViewer.sendItemInfoInterface(player, "@whi@Zyrox Commands ( @dre@" + commands.size() + " @whi@)", new ArrayList<>(), commands);

        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[]{"::commands", "::commands [rights]"};
    }

}
