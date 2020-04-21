package com.varrock.net.packet.impl;

import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.world.World;
import com.varrock.world.content.ProfileViewing;
import com.varrock.world.entity.impl.player.Player;

public class ViewProfilePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int index = packet.readLEShort();
		if (index < 0) {
			return;
		}
		Player other = World.getPlayerByIndex(index);
		if (other == null) {
			return;
		}
		if (other.getPosition().getDistance(player.getPosition()) > 5) {
			player.sendMessage("The other player is too far away from you.");
			return;
		}
		ProfileViewing.view(player, other);
	}
}
