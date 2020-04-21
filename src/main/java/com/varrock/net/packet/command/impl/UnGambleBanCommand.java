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
@CommandHeader(command = { "ungambleban"}, description = "Un gamble bans a player.")
public class UnGambleBanCommand extends Command {

    public UnGambleBanCommand() {
        super(PunishmentType.GAMBLE_BAN.minimumStaffRights);
    }

    @Override
    public boolean execute(Player player, String[] args) throws Exception {

        PunishmentType punishmentType = PunishmentType.GAMBLE_BAN;

        String name = Misc.formatPlayerName(String.join(" ", Arrays.copyOfRange(args, 0, args.length)));

        Player other = OfflineCharacter.getOfflineCharacter(name);

        if(other == null) {
            DialogueManager.sendStatement(player, "@red@"+name+" does not exist.");
            return true;
        }

        boolean successful = GameServer.punishmentManager.removePunishment(player, name, punishmentType);

        if(successful) {
            player.sendErrorMessage("You have successfully un-"+punishmentType.getName()+" "+name);
        } else {
            player.sendErrorMessage("Error! "+name+" is not "+punishmentType.getName());
        }
        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[] { "::ungambleban name" };
    }

}
