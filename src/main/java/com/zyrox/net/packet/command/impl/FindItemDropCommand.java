package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.entity.impl.npc.DropViewer;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = {"findmonster"}, description = "Find the monsters that drop certain item command!")
public class FindItemDropCommand extends Command {

    @Override
    public boolean execute(Player player, String[] args) throws Exception {

        if (player.dropCommandCooldown > System.currentTimeMillis()) {
            player.sendMessage("You can only use this command once every 10 seconds!");
            return false;
        }
        try {

            String search = "";

            for (int i = 0; i < args.length; i++) {
                search += args[i] + (i < args.length - 1 ? " " : "");
            }

            if (search.isEmpty()) {
                player.sendMessage("Error excuting command! try using ::getdrop monster name");
                return false;
            }

            new DropViewer(player, search, false);
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage("Error excuting command! try using ::getdrop monster name");
        }

        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[] {
                "::findmonster itemId"
        };
    }
}
