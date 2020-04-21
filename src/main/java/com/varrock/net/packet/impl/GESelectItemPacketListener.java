package com.varrock.net.packet.impl;

import com.varrock.model.Item;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.world.entity.impl.player.Player;

public class GESelectItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int item = packet.readUnsignedShort();
		if(item <= 0 || item >= ItemDefinition.getMaxAmountOfItems())
			return;
		ItemDefinition def = ItemDefinition.forId(item);
		if(def != null) {
			if(def.getValue() <= 0 || !Item.tradeable(item) || item == 995) {
				player.getPacketSender().sendMessage("This item can currently not be purchased or sold in the Grand Exchange.");
				return;
			}
		
		}
	}

}
