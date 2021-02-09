package com.zyrox.net.packet.impl;

import org.jboss.netty.buffer.ChannelBuffer;

import com.zyrox.GameServer;
import com.zyrox.net.packet.Packet;
import com.zyrox.net.packet.PacketListener;
import com.zyrox.util.Misc;
import com.zyrox.world.content.clan.ClanChatManager;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.entity.impl.player.Player;

public class SendClanChatMessagePacketListener implements PacketListener {
	
	@Override
	public void handleMessage(Player player, Packet packet) {
		/** Get method for the channel buffer. **/
		ChannelBuffer opcode = packet.getBuffer();
		/** Gets requested bytes from the buffer client > server **/
		int size = opcode.readableBytes();
		/** Check to flood **/
		if (size < 1 || size > 255) {
			System.err.println("blocked packet from sending from clan chat. Requested size="+size);
			return;
		}
		
		String clanMessage = packet.readString();
		/** Checks for null, invalid messages **/
		if(clanMessage == null || clanMessage.length() < 1 || clanMessage.length() > 255)
			return;
		
		if(GameServer.punishmentManager.isMuted(player)) {
			player.getPacketSender().sendMessage("You are muted and cannot chat.");
			return;
		}
		if (Misc.blockedWord(clanMessage)) {
			DialogueManager.sendStatement(player, "Your text contains forbidden words.");
			return;
		}
		ClanChatManager.sendMessage(player, clanMessage);
	}

//	@Override
//	public void handleMessage(Player player, Packet packet) {
//		String clanMessage = Misc.readString(packet.getBuffer());
//
//		if (clanMessage == null || clanMessage.length() <= 0 || packet.getSize() > Byte.MAX_VALUE || clanMessage.contains("::::")) 
//			return;
//
//		if (player.requiresUnlocking()) {
//			BankPin.init(player, false);
//			return;
//		}
//		if (player.isAccountCompromised()) {
//			player.sendMessage("@red@We have a reason to believe that this account was compromised.");
//			player.sendMessage("@red@Please contact a staff member to recover this account.");
//			return;
//		}
//
//		if(GameServer.punishmentManager.isMuted(player))
//			return;
//
//		if (Misc.blockedWord(clanMessage)) {
//			DialogueManager.sendStatement(player, "Your text contains forbidden words.");
//			return;
//		}
//
//		ClanChatManager.sendMessage(player, clanMessage);
//	}

}
