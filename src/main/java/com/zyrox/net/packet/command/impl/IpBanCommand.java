package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.GameServer;
import com.zyrox.model.OfflineCharacter;
import com.zyrox.model.punish.PunishmentType;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 7/26/2019
 **/
@CommandHeader(command = { "ipban"}, description = "IP bans a player.")
public class IpBanCommand extends Command {

    public IpBanCommand() {
        super(PunishmentType.IP_BANNED.minimumStaffRights);
    }

    @Override
    public boolean execute(Player player, String[] args) throws Exception {

        String name = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

        Player other = OfflineCharacter.getOfflineCharacter(name);

        if(other == null) {
            DialogueManager.sendStatement(player, "@red@"+name+" does not exist.");
            return true;
        }

        if(other.getHostAddress() == null) {
            player.sendMessage("Error loading this character file.");
            return true;
        }

        GameServer.punishmentManager.startPunishment(player, name, other.getHostAddress(), PunishmentType.IP_BANNED);

        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[] { "::ban name" };
    }

}
