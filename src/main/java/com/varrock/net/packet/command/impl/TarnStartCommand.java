package com.varrock.net.packet.command.impl;

import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.content.transportation.TeleportHandler;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = {"tarn"}, description = "Starts a instance of tarn.")
public class TarnStartCommand extends Command {

    public TarnStartCommand() {
        super(PlayerRights.PLAYER, false);
    }

    @Override
    public boolean execute(Player player, String[] args) throws Exception {
        TeleportHandler.startTarn(player);
        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[]{"::tarn"};
    }


}
