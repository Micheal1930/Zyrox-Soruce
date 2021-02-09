package com.zyrox.world.content;

import com.zyrox.util.Misc;
import com.zyrox.world.entity.impl.player.Player;

public class CharmBox {
	
	public static int blueCharm = 12163;
	public static int greenCharm = 12159;
	public static int redCharm = 12160;
	public static int goldCharm = 12158;
	
	
	public static void open (Player player) {
		if (!player.getInventory().contains(10025)) {//this should be fine only 1 box this time :P
			return;
		}
		int amount = 20 + Misc.getRandom(25);
		player.getInventory().delete(10025,1);
		player.getPacketSender().sendMessage("You open the charm box and receive some charms.");
		player.getInventory().add(blueCharm, amount, "Charm box");
		player.getInventory().add(redCharm, amount, "Charm box");
		player.getInventory().add(goldCharm, amount, "Charm box");
		player.getInventory().add(greenCharm, amount, "Charm box");
	}	

}
