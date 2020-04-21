package com.varrock.net.packet.command.impl;

import java.util.Arrays;

import com.varrock.GameServer;
import com.varrock.model.OfflineCharacter;
import com.varrock.model.punish.PunishmentType;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.util.Misc;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 7/26/2019
 **/
@CommandHeader(command = { "ban"}, description = "Bans a player.")
public class BanCommand extends Command {

    public BanCommand() {
        super(PunishmentType.BANNED.minimumStaffRights);
    }

    @Override
    public boolean execute(Player player, String[] args) throws Exception {

        String name = Misc.formatPlayerName(String.join(" ", Arrays.copyOfRange(args, 0, args.length)));

        Player other = OfflineCharacter.getOfflineCharacter(name);

        if(other == null) {
            DialogueManager.sendStatement(player, "@red@"+name+" does not exist.");
            return true;
        }

        GameServer.punishmentManager.startPunishment(player, name, name, PunishmentType.BANNED);

        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[] { "::ban name" };
    }

}
