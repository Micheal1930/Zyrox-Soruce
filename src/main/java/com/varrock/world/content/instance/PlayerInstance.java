package com.varrock.world.content.instance;

import java.util.ArrayList;

import com.varrock.engine.task.Task;
import com.varrock.model.GroundItem;
import com.varrock.model.Position;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

/**
 * Represents a player instance
 * 
 * @author 2012
 *
 */
public abstract class PlayerInstance {

	/**
	 * The npcs in the instance
	 */
	private ArrayList<NPC> npcs = new ArrayList<NPC>();

	/**
	 * The ground items in the instance
	 */
	private ArrayList<GroundItem> groundItems = new ArrayList<GroundItem>();

	/**
	 * The player name
	 */
	private String playerName;

	/**
	 * The height level
	 */
	private int heightLevel;

	/**
	 * Creating a player instance
	 * 
	 * @param player      the player
	 */
	public PlayerInstance(Player player) {
		this.playerName = player.getUsername();
	}

	/**
	 * Starting the instance
	 * 
	 * @param player the player
	 */
	public abstract void start(Player player);

	/**
	 * Gets the task
	 * 
	 * @param player the player
	 * @return the task
	 */
	public abstract Task getTask(Player player);

	/**
	 * Ends the instance
	 * 
	 * @param player the player
	 */
	public abstract void end(Player player);
	
	/**
	 * The player spawn position
	 * @return the position
	 */
	public abstract Position getPlayerSpawnPosition();
	
	/**
	 * Whether can teleport out
	 * @return teleporting out
	 */
	public abstract boolean canTeleportOut();
	
	/**
	 * Gets the region ids
	 * @return the reginio ids
	 */
	public abstract int[] getRegionIds();

	/**
	 * Gets the npcs
	 *
	 * @return the npcs
	 */
	public ArrayList<NPC> getNpcs() {
		return npcs;
	}

	/**
	 * Gets the groundItems
	 *
	 * @return the groundItems
	 */
	public ArrayList<GroundItem> getGroundItems() {
		return groundItems;
	}

	/**
	 * Gets the playerName
	 *
	 * @return the playerName
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Gets the heightLevel
	 *
	 * @return the heightLevel
	 */
	public int getHeightLevel() {
		return heightLevel;
	}

	/**
	 * Sets the heightLevel
	 *
	 * @param heightLevel the heightLevel
	 */
	public void setHeightLevel(int heightLevel) {
		this.heightLevel = heightLevel;
	}
}
