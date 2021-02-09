package com.zyrox.net.packet.impl;

import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.Packet;
import com.zyrox.net.packet.PacketListener;
import com.zyrox.util.Misc;
import com.zyrox.world.content.Debug;
import com.zyrox.world.content.auction_house.AuctionHouseManager;
import com.zyrox.world.entity.impl.player.Player;


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
