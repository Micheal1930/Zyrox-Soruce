package com.varrock.net.packet.impl;

import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.util.Misc;
import com.varrock.world.content.Debug;
import com.varrock.world.entity.impl.player.Player;

/**
 * This packet manages the input taken from chat box interfaces that allow
 * input, such as withdraw x, bank x, enter name of friend, etc.
 * 
 * @author Gabriel Hannason
 */

public class EnterInputPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case ENTER_SYNTAX_OPCODE:
			String name = Misc.readString(packet.getBuffer());
			
			if (name == null) {
				return;
			}

			if (player.getInputHandling() != null) {
				Debug.write(player.getName(), "EnterInputPacketListener", new String[] {
						"input: "+player.getInputHandling(),
				});
				player.getInputHandling().handleSyntax(player, name);
			}

			player.setInputHandling(null);
			break;
		case ENTER_AMOUNT_OPCODE:
			long amount = packet.readInt();
			
			if (amount <= 0) {
				return;
			}

			System.out.println(player.getInputHandling());
			
			if (player.getInputHandling() != null) {
				Debug.write(player.getName(), "EnterInputPacketListener", new String[] {
						"input: "+player.getInputHandling(),
				});
				if(!player.getInputHandling().handleAmount(player, (int) amount)) {
					player.setInputHandling(null);
				}
			}

			break;
		}
	}

	public static final int ENTER_AMOUNT_OPCODE = 208, ENTER_SYNTAX_OPCODE = 60;
}
