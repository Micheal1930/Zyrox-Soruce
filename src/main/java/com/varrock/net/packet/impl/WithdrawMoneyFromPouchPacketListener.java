package com.varrock.net.packet.impl;

import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.world.content.Debug;
import com.varrock.world.content.MoneyPouch;
import com.varrock.world.entity.impl.player.Player;

public class WithdrawMoneyFromPouchPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int amount = packet.readInt();

		Debug.write(player.getName(), "WtihdrawMoneyPouch", new String[] {
				"amount: "+amount,
		});

		MoneyPouch.withdrawMoney(player, amount);
	}

}
