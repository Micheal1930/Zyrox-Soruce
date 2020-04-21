package com.varrock.net.packet.impl;

import com.varrock.model.PlayerRights;
import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.world.content.auction_house.AuctionHouseManager;
import com.varrock.world.content.auction_house.AuctionHouseSortType;
import com.varrock.world.content.interfaces.StartScreen2;
import com.varrock.world.entity.impl.player.Player;


public class DropDownPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int dropdownId = packet.readUnsignedShort();
        int identifier = packet.readUnsignedByte();

        if(player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.DEVELOPER) {
            player.sendMessage("dropdownId="+dropdownId+",identifier="+identifier);
        }

        switch(dropdownId) {
            case 33137:
                player.auctionHouseSortType = AuctionHouseSortType.getTypeForIdentifier(identifier);
                AuctionHouseManager.reload(player);
                break;
            case 28031:
                StartScreen2.handleDropdown(player, identifier);
                break;
        }
    }

    public static final int OPCODE = 184;
}
