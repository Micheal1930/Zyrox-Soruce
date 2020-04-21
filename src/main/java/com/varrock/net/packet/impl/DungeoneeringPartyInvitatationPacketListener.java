package com.varrock.net.packet.impl;

import com.varrock.model.Locations.Location;
import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

public class DungeoneeringPartyInvitatationPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		String plrToInvite = Misc.readString(packet.getBuffer());
		if(plrToInvite == null || plrToInvite.length() <= 0)
			return;
		plrToInvite = Misc.formatText(plrToInvite);
		if(player.getLocation() == Location.DUNGEONEERING) {
			if(player.getMinigameAttributes().getDungeoneeringAttributes().getParty() == null || player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner() == null)
				return;
			player.getPacketSender().sendInterfaceRemoval();
			if(player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner() != player) {
				player.getPacketSender().sendMessage("Only the party leader can invite other players.");
				return;
			}
			Player invite = World.getPlayerByName(plrToInvite);
			if(invite == null) {
				player.getPacketSender().sendMessage("That player is currently not online.");
				return;
			}
			player.getMinigameAttributes().getDungeoneeringAttributes().getParty().invite(invite);
		}
	}
}
