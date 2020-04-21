package com.varrock.world.content.skill.impl.slayer;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.varrock.world.entity.impl.player.Player;

/**
 * Handles the slayer log
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class SlayerLog {

	/**
	 * Sending the slayer log
	 * 
	 * @param player the player
	 */
	public static void sendSlayerLog(Player player) {
		/*
		 * The slot
		 */
		int slot = 59237;

		for (int i = 59237; i < 59734; i++) {
			player.getPacketSender().sendString(i, "");
		}
		/*
		 * The slayer list
		 */
		Iterator<Entry<String, Integer>> it = player.getSlayer().getLog().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			String name = (String) pair.getKey();
			int amount = (int) pair.getValue();
			player.getPacketSender().sendString(slot, name);
			player.getPacketSender().sendString(slot + 1, NumberFormat.getInstance().format(amount));
			player.getPacketSender().sendString(slot + 2, "N/A");
			slot += 5;
		}
		player.getPacketSender().sendInterface(59234);
	}

}