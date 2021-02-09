package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = {"vorkath"}, description = "Starts a instance of vorkath.")
public class VorkathStartCommand extends Command {

    @Override
    public boolean execute(Player player, String[] args) throws Exception {
        TeleportHandler.startVorkath(player);
        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[]{"::vorkath"};
    }


}
