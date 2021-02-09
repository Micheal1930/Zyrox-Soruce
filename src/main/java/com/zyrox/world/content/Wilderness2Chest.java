package com.zyrox.world.content;

import com.zyrox.model.Animation;
import com.zyrox.model.GameObject;
import com.zyrox.model.Graphic;
import com.zyrox.model.Item;
import com.zyrox.model.PlayerRights;
import com.zyrox.util.Misc;
import com.zyrox.util.RandomUtility;
import com.zyrox.world.entity.impl.player.Player;

public class Wilderness2Chest {

	public static void handleChest(final Player p, final GameObject chest) {
		if (!p.getClickDelay().elapsed(2000)) {
			return;
		}

		if (!p.getInventory().contains(3135)) {
			p.getPacketSender().sendMessage("This chest can only be opened with the Wilderness Key.");
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
			p.getInventory().delete(3135, 1);
		} else {
			p.getPacketSender().sendMessage("Your Wilderness Key has been saved as a donator benefit");
		}
		
		p.getPacketSender().sendMessage("You open the chest..");

		Item loot = new Item(WildernessDrops.DROPS.get(Misc.random(WildernessDrops.DROPS.size() - 1)), 1);

		p.getInventory().add(loot, "Wilderness Chest loot");

		p.getInventory().add(995, 5000 + RandomUtility.RANDOM.nextInt(10000), "Wilderness chest");
	}
	
}
