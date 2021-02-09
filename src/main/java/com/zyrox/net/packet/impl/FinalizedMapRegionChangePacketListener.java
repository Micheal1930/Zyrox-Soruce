package com.zyrox.net.packet.impl;

import com.zyrox.model.GameObject;
import com.zyrox.net.packet.Packet;
import com.zyrox.net.packet.PacketListener;
import com.zyrox.world.content.CustomObjects;
import com.zyrox.world.entity.impl.player.Player;

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