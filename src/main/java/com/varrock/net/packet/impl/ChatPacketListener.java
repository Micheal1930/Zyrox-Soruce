package com.varrock.net.packet.impl;

import com.varrock.GameServer;
import com.varrock.GameSettings;
import com.varrock.model.Flag;
import com.varrock.model.Locations;
import com.varrock.model.ChatMessage.Message;
import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.util.Misc;
import com.varrock.util.TextUtils;
import com.varrock.world.content.BankPin;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.entity.impl.player.Player;

/**
 * This packet listener manages the spoken text by a player.
 * 
 * @author relex lawl
 */

public class ChatPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int effects = packet.readUnsignedByteS();
		int color = packet.readUnsignedByteS();
		int size = packet.getSize();

		if (size < 0 || size > 80) {
			System.out.println("Trying to crash size");
			TextUtils.writeToFile("./sizehax.txt", "Trying to crash with size: " + size);
			return;
		}

		byte[] text = packet.readReversedBytesA(size);

		if (player.isHidePlayer()) {
			return;
		}

		if(GameServer.punishmentManager.isMuted(player)) {
			return;
		}

		if (player.requiresUnlocking()) {
			BankPin.init(player, false);
			return;
		}
		if (player.isAccountCompromised()) {
			player.sendMessage("@red@We have a reason to believe that this account was compromised.");
			player.sendMessage("@red@Please contact a staff member to recover this account.");
			return;
		}
		if (GameSettings.MUTE_NEWCOMERS && Misc.getMinutesPlayed(player) <= 5) {
			player.sendMessage("As a newcomer, you are muted until 5 minutes of gameplay.");
			return;
		}
		
		String message = new String(text);

		if (Misc.blockedWord(message)) {
			DialogueManager.sendStatement(player, "Your text contains forbidden words.");
			return;
		}

		player.getChatMessages().set(new Message(color, effects, text));
		player.getUpdateFlag().flag(Flag.CHAT);
	}

}
