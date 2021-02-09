package com.zyrox.world.content;

import com.zyrox.model.Animation;
import com.zyrox.model.GameObject;
import com.zyrox.model.Graphic;
import com.zyrox.model.Item;
import com.zyrox.model.PlayerRights;
import com.zyrox.util.Misc;
import com.zyrox.util.RandomUtility;
import com.zyrox.world.entity.impl.player.Player;

public class StoneChest {

	public static void handleChest(final Player p, final GameObject chest) {
		if (!p.getClickDelay().elapsed(2000)) {
			return;
		}

		if (!p.getInventory().contains(52428)) {
			p.getPacketSender().sendMessage("This chest can only be opened with the stone key.");
			return;
		}

		p.performAnimation(new Animation(7253));
		p.performGraphic(new Graphic(6));
		
		int random = 0;
		
		if (PlayerRights.isExecutiveDonator(p)) {
			random = 80;
		} else if (PlayerRights.isPlatinumDonator(p)) {
			random = 80;
		} else if (PlayerRights.isExtremeDonator(p)) {
			random = 80;
		} else if (PlayerRights.isSuperDonator(p)) {
			random = 85;
		} else if (PlayerRights.isRegularDonator(p)) {
			random = 90;
		}
		
		if (random == 0 || Misc.getRandom(100) < random) {
			p.getInventory().delete(52428, 1);
		} else {
			p.getPacketSender().sendMessage("Stone Key has been saved as a donator benefit");
		}
		
		p.getPacketSender().sendMessage("You open the chest..");

		Item[] loot = itemRewards[Misc.getRandom(itemRewards.length - 1)];
		
		for (Item item : loot) {
			p.getInventory().add(item, "Stone Chest loot");
		}
		
		p.getInventory().add(995, 5000 + RandomUtility.RANDOM.nextInt(10000), "Stone chest");
	}

	private static final Item[][] itemRewards =  {
			{new Item(995, 1000000)}, //bronze box
			{new Item(995, 1000000)}, //bronze box
			{new Item(995, 1500000)}, //bronze box
			{new Item(995, 1200000)}, //bronze box
			{new Item(995, 1300000)}, //gold box
			{new Item(995, 1600000)}, //gold box
			{new Item(18339, 1)}, // gold bag
			{new Item(554, 50), new Item(555, 50), new Item(556, 50), new Item(557, 50), new Item(558, 50), new Item(559, 50), new Item(560, 10), new Item(561, 10), new Item(562, 10), new Item(563, 10), new Item(564, 10)}, //set 4 Full rune set
			{new Item(1631, 1), new Item(454, 1000)}, //set 5 Coal
			{new Item(1615, 1), new Item(1601, 1), new Item(1603, 1)}, //set 6 Cut gems
			{new Item(1631, 1), new Item(985, 1), new Item(995, 7500)}, //set 7 Crystal Key 1
			{new Item(1631, 1), new Item(987, 1), new Item(995, 7500)}, //set 9 Crystal Key 2
			{new Item(1631, 1), new Item(441, 1000)}, //set 10 Iron Ore
			{new Item(995, 1500000)},
			{new Item(995, 1500000)},
			{new Item(995, 1500000)},
			
		};
	
}
