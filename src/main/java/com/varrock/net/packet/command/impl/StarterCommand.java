package com.varrock.net.packet.command.impl;

import java.util.ArrayList;
import java.util.List;

import com.varrock.model.Item;
import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.net.packet.impl.CommandPacketListener;
import com.varrock.world.entity.impl.npc.DropViewer;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = {"starter"}, description = "Opens a starter guide")
public class StarterCommand extends Command {

    @Override
    public boolean execute(Player player, String[] args) throws Exception {

        player.getPacketSender().sendString(1, "https://varrock.io/forums/");

        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[]{"::starter"};
    }

}
