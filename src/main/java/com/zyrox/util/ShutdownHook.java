package com.zyrox.util;

import java.util.logging.Logger;

import com.zyrox.GameServer;
import com.zyrox.world.World;
import com.zyrox.world.content.auction_house.AuctionHouseManager;
import com.zyrox.world.content.clan.ClanChatManager;
import com.zyrox.world.entity.impl.player.Player;
import com.zyrox.world.entity.impl.player.PlayerHandler;

public class ShutdownHook extends Thread {

	/**
	 * The ShutdownHook logger to print out information.
	 */
	private static final Logger logger = Logger.getLogger(ShutdownHook.class.getName());

	@Override
	public void run() {
		logger.info("The shutdown hook is processing all required actions...");
		//World.savePlayers();
		GameServer.setUpdating(true);
		for (Player player : World.getPlayers()) {
			if (player != null) {
			//	World.deregister(player);
				PlayerHandler.handleLogout(player);
			}
		}
		World.getWell().save();
		ClanChatManager.save();
		AuctionHouseManager.saveAll();
		logger.info("The shudown hook actions have been completed, shutting the server down...");
	}
}
