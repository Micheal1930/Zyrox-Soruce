package com.varrock.net.packet.command.impl;

import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.entity.impl.npc.DropViewer;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = {"testdrop"}, description = "Tests your luck the NPC of your choice!")
public class TestDrop extends Command {

    public TestDrop() {
        super(PlayerRights.DEVELOPER, false);
    }

    @Override
    public boolean execute(Player player, String[] args) throws Exception {

        if (player.dropCommandCooldown > System.currentTimeMillis()) {
            player.sendMessage("You can only use this command once every 10 seconds!");
            return false;
        }
        try {

            int npcId = Integer.valueOf(args[0]);

            int totalDrops = Integer.valueOf(args[1]);

            if (totalDrops > 10000)
                totalDrops = 10000;
            if (totalDrops < 1)
                totalDrops = 1;

            new DropViewer(player, npcId, totalDrops);
        } catch (Exception e) {
            player.sendMessage("Error excuting command! try using ::testdrop npcid amountofkills");
        }
        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[]{"::testdrop"};
    }

}
