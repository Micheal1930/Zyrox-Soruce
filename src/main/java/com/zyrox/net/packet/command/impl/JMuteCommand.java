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
@CommandHeader(command = { "jmute"}, description = "J mutes a player.")
public class JMuteCommand extends Command {

    public JMuteCommand() {
        super(PunishmentType.J_MUTED.minimumStaffRights);
    }

    @Override
    public boolean execute(Player player, String[] args) throws Exception {

        String name = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

        Player other = OfflineCharacter.getOfflineCharacter(name);

        if(other == null) {
            DialogueManager.sendStatement(player, "@red@"+name+" does not exist.");
            return true;
        }

        if(other.getjSerial() == null) {
            player.sendMessage("Error loading this character file.");
            return true;
        }

        GameServer.punishmentManager.startPunishment(player, name, other.getjSerial(), PunishmentType.J_MUTED);

        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[] { "::jmute name" };
    }

}
