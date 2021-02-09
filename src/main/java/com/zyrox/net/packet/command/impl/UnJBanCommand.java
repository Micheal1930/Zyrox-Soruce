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
@CommandHeader(command = { "unjban"}, description = "Un-j bans a player.")
public class UnJBanCommand extends Command {

    public UnJBanCommand() {
        super(PunishmentType.J_BANNED.minimumStaffRights);
    }

    @Override
    public boolean execute(Player player, String[] args) throws Exception {

        PunishmentType punishmentType = PunishmentType.J_BANNED;

        String name = Misc.formatPlayerName(String.join(" ", Arrays.copyOfRange(args, 0, args.length)));

        Player other = OfflineCharacter.getOfflineCharacter(name);

        if(other == null) {
            DialogueManager.sendStatement(player, "@red@"+name+" does not exist.");
            return true;
        }

        boolean successful = GameServer.punishmentManager.removePunishment(player, other.getjSerial(), punishmentType);

        if(successful) {
            player.sendErrorMessage("You have successfully un-"+punishmentType.getName()+" "+name);
        } else {
            player.sendErrorMessage("Error! "+name+" is not "+punishmentType.getName());
        }
        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[] { "::unjban name" };
    }

}
