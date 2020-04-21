package com.varrock.net.packet.impl;

import com.varrock.model.PlayerRights;
import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.util.Misc;
import com.varrock.world.content.Debug;
import com.varrock.world.content.auction_house.AuctionHouseManager;
import com.varrock.world.entity.impl.player.Player;


public class ItemSearchPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {

        int identifier = 0;

        String itemName = Misc.readString(packet.getBuffer());

        Debug.write(player.getName(), "ItemSearchPacketListener", new String[] {
                "search: "+itemName,
        });

        if(player.getRights() == PlayerRights.OWNER) {
            player.sendMessage("identifier="+identifier+",itemName="+itemName);
        }

        switch(identifier) {
            case 0:
                AuctionHouseManager.searchForItem(player, itemName);
                break;
        }
    }

    public static final int OPCODE = 182;
}
