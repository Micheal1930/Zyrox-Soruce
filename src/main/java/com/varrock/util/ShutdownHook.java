package com.varrock.util;

import java.util.logging.Logger;

import com.varrock.GameServer;
import com.varrock.world.World;
import com.varrock.world.content.auction_house.AuctionHouseManager;
import com.varrock.world.content.clan.ClanChatManager;
import com.varrock.world.entity.impl.player.Player;
import com.varrock.world.entity.impl.player.PlayerHandler;

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
