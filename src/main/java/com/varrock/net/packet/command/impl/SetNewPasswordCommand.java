package com.varrock.net.packet.command.impl;

import java.util.Arrays;

import com.varrock.model.OfflineCharacter;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.saving.PlayerSaving;
import com.varrock.util.Encrypt;
import com.varrock.util.Misc;
import com.varrock.util.RandomEvent;
import com.varrock.world.World;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 7/26/2019
 **/
@CommandHeader(command = { "setnewpassword" }, description = "Sets the password of a player.")
public class SetNewPasswordCommand extends NameCommand {

    public SetNewPasswordCommand() {
        super("Finch", "Jonny");
    }

    @Override
    public boolean execute(Player player, String[] args) throws Exception {
        
        if(args.length == 0) {
            player.sendMessage("You must use this command as: "+getUsage()[0]);
            return true;
        }

        String username = Misc.formatPlayerName(String.join(" ", Arrays.copyOfRange(args, 0, args.length)));

        Player target = World.getPlayerByName(username);

        if(target == null) {
            target = OfflineCharacter.getOfflineCharacter(username);

            if(target == null) {
                DialogueManager.sendStatement(player, "@red@"+username+" does not exist.");
                return true;
            }

        }

        String newPassword = RandomEvent.adj[Misc.random(RandomEvent.adj.length - 1)] + Misc.random(0, 99);

        target.setPassword(newPassword);
        target.getPasswordNew().setRealPassword(newPassword);

        PlayerSaving.getSaving().save(target);

        player.sendMessage("You have reset " + username + "'s password to: <col=ff0000>" + newPassword);

        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[] { "::setpassword" };
    }

}
