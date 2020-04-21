package com.varrock.net.packet.impl;

import com.varrock.model.RegionInstance.RegionInstanceType;
import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.world.content.CustomObjects;
import com.varrock.world.content.Sounds;
import com.varrock.world.entity.impl.GroundItemManager;
import com.varrock.world.entity.impl.player.Player;


public class RegionChangePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
           // System.out.println("REGION CHANGE PACKET BEING CALLED");
		if(player.isAllowRegionChangePacket()) {
           //         System.out.println("PLAYER IS ALLOWED TO CHANGE REGION: "+ player.getUsername());
			CustomObjects.handleRegionChange(player);
			GroundItemManager.handleRegionChange(player);
			Sounds.handleRegionChange(player);
			player.getTolerance().reset();
			//Hunter.handleRegionChange(player);
			if(player.getRegionInstance() != null && player.getPosition().getX() != 1 && player.getPosition().getY() != 1) {
				if(player.getRegionInstance().equals(RegionInstanceType.BARROWS) || player.getRegionInstance().equals(RegionInstanceType.WARRIORS_GUILD))
					player.getRegionInstance().destruct();
			}
			
			player.getNpcFacesUpdated().clear();
			
			player.setRegionChange(false).setAllowRegionChangePacket(false);
		}
	}
}
