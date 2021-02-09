package com.zyrox.net.packet.impl;

import com.zyrox.net.packet.Packet;
import com.zyrox.net.packet.PacketListener;
import com.zyrox.world.content.Debug;
import com.zyrox.world.content.MoneyPouch;
import com.zyrox.world.entity.impl.player.Player;

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
