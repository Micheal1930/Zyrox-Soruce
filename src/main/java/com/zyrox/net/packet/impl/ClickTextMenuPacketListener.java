package com.zyrox.net.packet.impl;

import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.Packet;
import com.zyrox.net.packet.PacketListener;
import com.zyrox.world.content.clan.ClanChatManager;
import com.zyrox.world.entity.impl.player.Player;

public class ClickTextMenuPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {

		int interfaceId = packet.readShort();
		int menuId = packet.readByte();

		if(player.getRights() == PlayerRights.DEVELOPER) {
			player.getPacketSender().sendConsoleMessage("Clicked text menu: "+interfaceId+", menuId: "+menuId);
		}
		
		if (interfaceId >= 20472 && interfaceId <= 20488) {
			player.getPresets().handleMenu(interfaceId, menuId);
			return;
		}
		
		if(interfaceId >= 29344 && interfaceId <= 29443) { // Clan chat list
			int index = interfaceId - 29344;
			ClanChatManager.handleMemberOption(player, index, menuId);
			return;
		}
		
	}

	public static final int OPCODE = 222;
}
