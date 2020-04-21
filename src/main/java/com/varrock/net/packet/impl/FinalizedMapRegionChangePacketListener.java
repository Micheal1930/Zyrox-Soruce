package com.varrock.net.packet.impl;

import com.varrock.model.GameObject;
import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.world.content.CustomObjects;
import com.varrock.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player's region has been loaded.
 * 
 * @author relex lawl
 */

public class FinalizedMapRegionChangePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		for(GameObject object : CustomObjects.OBJECTS_TO_DELETE) {
			player.getPacketSender().sendObjectRemoval(object);
		}
	}
}