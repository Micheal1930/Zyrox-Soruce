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
@CommandHeader(command = { "unmassban"}, description = "Un-mass bans a player.")
public class UnMassBanCommand extends Command {

    public UnMassBanCommand() {
        super(PunishmentType.J_BANNED.minimumStaffRights);
    }

    @Override
    public boolean execute(Player player, String[] args) throws Exception {

        String name = Misc.formatPlayerName(String.join(" ", Arrays.copyOfRange(args, 0, args.length)));

        Player other = OfflineCharacter.getOfflineCharacter(name);

        if(other == null) {
            DialogueManager.sendStatement(player, "@red@"+name+" does not exist.");
            return true;
        }

        for(PunishmentType punishmentType : PunishmentType.values()) {
            if(punishmentType == PunishmentType.GAMBLE_BAN)
                continue;
            GameServer.punishmentManager.removePunishment(player, punishmentType.getDataForPlayer(other), punishmentType);
        }

        player.sendMessage("You have freed "+name+" from narnia.");

        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[] { "::unmassban name" };
    }

}
