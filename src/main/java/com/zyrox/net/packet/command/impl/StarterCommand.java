package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = {"starter"}, description = "Opens a starter guide")
public class StarterCommand extends Command {

    @Override
    public boolean execute(Player player, String[] args) throws Exception {

        player.getPacketSender().sendString(1, "https://zyrox.org/forums/");

        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[]{"::starter"};
    }

}
