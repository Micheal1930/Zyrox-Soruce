package com.varrock.net.packet.command.impl;

import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.content.transportation.TeleportHandler;
import com.varrock.world.entity.impl.player.Player;

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
