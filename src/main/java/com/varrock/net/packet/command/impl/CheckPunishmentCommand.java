package com.varrock.net.packet.command.impl;

import java.util.Arrays;

import com.varrock.GameServer;
import com.varrock.model.punish.PunishmentType;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.util.Misc;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 7/26/2019
 **/
@CommandHeader(command = { "checkpunish"}, description = "Checks a players punishments.")
public class CheckPunishmentCommand extends Command {

    public CheckPunishmentCommand() {
        super(PunishmentType.BANNED.minimumStaffRights);
    }

    @Override
    public boolean execute(Player player, String[] args) throws Exception {

        String name = Misc.formatPlayerName(String.join(" ", Arrays.copyOfRange(args, 0, args.length)));

        GameServer.punishmentManager.checkPunishments(player, name);

        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[] { "::checkpunish name" };
    }

}
