package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.GameServer;
import com.zyrox.model.OfflineCharacter;
import com.zyrox.model.punish.PunishmentType;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.util.Misc;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.entity.impl.player.Player;

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
