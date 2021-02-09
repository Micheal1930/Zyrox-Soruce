package com.zyrox.world.content.instance;

import java.util.ArrayList;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.GroundItem;
import com.zyrox.model.Position;
import com.zyrox.world.World;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.entity.impl.GroundItemManager;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Handles the instancing
 * 
 * @author 2012
 *
 */
public class PlayerInstanceManager {

	/**
	 * The instances
	 */
	private static ArrayList<PlayerInstance> instances = new ArrayList<PlayerInstance>();

	/**
	 * The player instance
	 */
	private PlayerInstance instance;

	/**
	 * Starting an instance
	 * 
	 * @param player      the player
	 * @param instance    the instance
	 * @param heightLevel the height level
	 */
	public static void startInstance(Player player, PlayerInstance instance, int heightLevel) {
		/*
		 * Check teleport
		 */
		if (!TeleportHandler.checkReqs(player, instance.getPlayerSpawnPosition())) {
			return;
		}
		if (!TeleportHandler.checkWildernessLevel(player)) {
			return;
		}
		/*
		 * Cancel actions
		 */
		TeleportHandler.cancelCurrentActions(player);
		/*
		 * End instance
		 */
		endInstance(player);
		System.out.println("");
		System.out.println("1. Start Player instance: " + player.getUsername());
		/*
		 * Set instance
		 */
		player.getPlayerInstance().setInstance(instance);
		/*
		 * Add instance
		 */
		instances.add(instance);
		System.out.println("2. Added instance. Instance list size: " + instances.size());
		/*
		 * The spawn position
		 */
		Position pos = instance.getPlayerSpawnPosition().copy();
		/*
		 * Sets the height
		 */
		pos.setZ(getInstanceHeight(player, heightLevel));
		/*
		 * Set height
		 */
		instance.setHeightLevel(pos.getZ());
		System.out.println("3. Instance Height: " + instance.getHeightLevel());
		/*
		 * Move player
		 */
		player.moveTo(pos);
		/*
		 * Start instance
		 */
		instance.start(player);
		/*
		 * The task
		 */
		Task task = instance.getTask(player);
		/*
		 * Start task
		 */
		if (task != null) {
			TaskManager.submit(task);
		}
		/*
		 * Starts region task
		 */
		TaskManager.submit(new Task(5) {

			@Override
			protected void execute() {
				if (!checkIfInRegion(player)) {
					endInstance(player);
					stop();
				}
			}
		});
	}

	/**
	 * Checks if is in region
	 * 
	 * @param player the player
	 * @return the region
	 */
	private static boolean checkIfInRegion(Player player) {
		if (player.getPlayerInstance().getInstance() == null) {
			return false;
		}
		for (int region : player.getPlayerInstance().getInstance().getRegionIds()) {
			if (player.getRegionID() == region) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Ending an instance
	 * 
	 * @param player the player
	 */
	public static void endInstance(Player player) {
		/*
		 * No instance
		 */
		if (player.getPlayerInstance().getInstance() == null) {
			return;
		}
		/*
		 * The instance
		 */
		PlayerInstance instance = player.getPlayerInstance().getInstance();
		/*
		 * The task
		 */
		Task task = instance.getTask(player);
		System.out.println("4. ended instance: " + player.getUsername() + ". Height: " + instance.getHeightLevel());
		/*
		 * The task
		 */
		if (task != null) {
			task.stop();
		}
		/*
		 * Remove npcs
		 */
		for (NPC npc : instance.getNpcs()) {
			World.deregister(npc);
		}
		/*
		 * Remove ground items
		 */
		for (GroundItem item : instance.getGroundItems()) {
			GroundItemManager.remove(item, true);
		}
		/*
		 * Reset instance
		 */
		removeInstanceByUsername(player.getUsername());
		player.getPlayerInstance().setInstance(null);
	}

	/**
	 * Removing npcs on height
	 * 
	 * @param id          the id
	 * @param heightLevel the height level
	 */
	public static void removeAllNpcsOnHeight(int id, int heightLevel) {
		for (int i = 0; i < 100; i++) {
			NPC npc = World.getNpcByIdAndHeight(id, heightLevel);
			if (npc != null) {
				World.deregister(npc);
			}
		}
	}

	/**
	 * Removing an instance by username
	 * 
	 * @param name the name
	 */
	private static void removeInstanceByUsername(String name) {
		for (PlayerInstance instance : instances) {
			if (instance == null) {
				continue;
			}
			if (instance.getPlayerName().equalsIgnoreCase(name)) {
				instances.remove(instance);
				System.out.println("5. Found instance in array and removed by name: " + name);
				break;
			}
		}
		instances.trimToSize();
		System.out.println("6. Instance list size: " + instances.size());
	}

	/**
	 * Gets the instance height
	 * 
	 * @param player      the player
	 * @param heightLevel the height level
	 * @return the height
	 */
	private static int getInstanceHeight(Player player, int heightLevel) {
		for (int i = 4; i < 2048; i += 4) {
			if (World.getPlayerHeight(i) == null) {
				return i;
			}
		}
		int base = (4 + heightLevel);
		return base + (instances.size() * base);
	}

	/**
	 * Gets the instance
	 *
	 * @return the instance
	 */
	public PlayerInstance getInstance() {
		return instance;
	}

	/**
	 * Sets the instance
	 *
	 * @param instance the instance
	 */
	public void setInstance(PlayerInstance instance) {
		this.instance = instance;
	}
}
