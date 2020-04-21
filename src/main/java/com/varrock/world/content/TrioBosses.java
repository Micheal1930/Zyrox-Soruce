package com.varrock.world.content;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.GroundItem;
import com.varrock.model.Item;
import com.varrock.model.Position;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.transportation.TeleportHandler;
import com.varrock.world.entity.impl.GroundItemManager;
import com.varrock.world.entity.impl.player.Player;

/*
 * Halloween event for 2016
 * @author Ajw www.simplicityps.org
 * 
 */

public class TrioBosses {
	
	//Npc ids that will drop keys
	public static int skeletonId = 1973;
	public static int zombieId = 75;
	public static int ghostId = 103;
	
	//Item ids that will be dropped
	public static int skeletonKey = 605;
	public static int ghostKey = 993;
	public static int zombieKey = 1590;
	
	//useless, just needed to write down obejct id
	public static int chest = 2079;

	//arrays that hold the rare and common chest loots
	public static int rareLoots[] = {995};
	
	public static int commonLoots[] = {1334, 11126};
	
	
	/*
	 * Start methods below
	 */
	
	
	/*
	 * Grabs a random item from aray
	 */
	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	
	/*
	 * Opening the chest with suspense, give reward
	 */
	public static void openChest(Player player) {
		if (player.getInventory().contains(605) && player.getInventory().contains(993) && player.getInventory().contains(1590)) {   
		
			TaskManager.submit(new Task(2, player, false) {
			@Override
			public void execute() {
				player.performAnimation(new Animation(6387));
				player.getPacketSender().sendMessage("Opening Chest...Good Luck!");
				player.getInventory().delete(605, 1);
				player.getInventory().delete(993, 1);
				player.getInventory().delete(1590, 1);
				giveReward(player);
				this.stop();
			}
		});
      } else {
	/*
	 * Player does not have all the keys
	 */
    	  player.getPacketSender().sendMessage("You need all the keys to open the chest!");
    	  return;
      }
	 
	}
	
	/*
	 * Who ever added this needs shooting from Rob Pk
	 */
	/*public static void eatPumpkin(Player player) {
		player.getInventory().delete(1959, 1);
		player.performAnimation(new Animation(865));
		player.performGraphic(new Graphic(199, GraphicHeight.HIGH));
		player.forceChat("Happy Halloween everyone from "+player.getUsername());
	}*/
	
	/*
	 * Handles teleporting into the event area
	 */
	public static void teleIn(Player player) {
		TeleportHandler.teleportPlayer(player, new Position(2575, 9874), player.getSpellbook().getTeleportType());
		player.getPacketSender().sendMessage("Welcome to the @or2@Trio Bosses</col>. Obtain all 3 keys to open the chest!");
	}
	
	/*
	 * Gives loot from chest
	 */
	public static void giveReward(Player player) {
			
			if (Misc.getRandom(20) == 5) {
			/*
			 * Give a rare Loot
			 */
			int rareDrops = getRandomItem(rareLoots);
			player.getInventory().add(rareDrops, 25000000, "Trio bosses");
			World.sendMessage("@red@[Trio Bosses]</col> "+player.getUsername()+ " has received a 25M loot");

		} else {
			/*
			 * Give Common Loot
			 */
			
			int commonDrops = getRandomItem(commonLoots);
			player.getInventory().add(995, 5000 + Misc.getRandom(200000), "Trio bosses");
			player.getInventory().add(commonDrops, 10 + Misc.getRandom(20), "Trio bosses");
		 }
			
	}
	
	/*
	 * Handles the skeleton npc drops
	 */
	public static void handleSkeleton(Player player, Position pos) {
		if (Misc.getRandom(50) == 25) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(skeletonKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have received a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles the ghost npc drops
	 */
	public static void handleGhost(Player player, Position pos) {
		if (Misc.getRandom(50) == 25) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(ghostKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have received a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}

	/*
	 * Handles the ghost npc drops
	 */
	public static void handleZombie(Player player, Position pos) {
		if (Misc.getRandom(50) == 25) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(zombieKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have received a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}

}
