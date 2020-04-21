package com.varrock.net.packet.impl;

import com.varrock.model.definitions.NpcDefinition;
import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.world.entity.impl.npc.DropViewer;
import com.varrock.world.entity.impl.player.Player;

public class ExamineNpcPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int npc = packet.readShort();
		if(npc <= 0) {
			return;
		}
		NpcDefinition npcDef = NpcDefinition.forId(npc);
		if(npcDef != null) {
			player.getPacketSender().sendMessage(npcDef.getExamine() + " "+ (player.isSpecialStaff() ? ("id="+npcDef.getId()+"") : ""));

			if(npcDef.isAttackable())
				new DropViewer(player, npcDef.getName(), true).setSendsErrorMessage(false);
		}
	}

}
