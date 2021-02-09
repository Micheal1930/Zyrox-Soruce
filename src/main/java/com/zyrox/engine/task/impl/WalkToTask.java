package com.zyrox.engine.task.impl;

import com.zyrox.model.Position;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Represents a movement action for a game character.
 * @author Gabriel Hannason
 */

public class WalkToTask {

	public interface FinalizedMovementTask {
		public void execute();
	}
	
	/**
	 * The WalkToTask constructor.
	 * @param entity			The associated game character.
	 * @param destination		The destination the game character will move to.
	 * @param finalizedTask		The task a player must execute upon reaching said destination.
	 */
	public WalkToTask(Player entity, Position destination, int distance, FinalizedMovementTask finalizedTask) {
		this(entity, destination, distance, distance, finalizedTask);
	}
	
	/**
	 * The WalkToTask constructor.
	 * @param entity			The associated game character.
	 * @param destination		The destination the game character will move to.
	 * @param finalizedTask		The task a player must execute upon reaching said destination.
	 */
	public WalkToTask(Player entity, Position destination, int xLength, int yLength, FinalizedMovementTask finalizedTask) {
		this.player = entity;
		this.destination = destination;
		this.finalizedTask = finalizedTask;
		
		minX = destination.getX() - xLength;
		maxX = destination.getX() + xLength;
		minY = destination.getY() - yLength;
		maxY = destination.getY() + yLength;
	}
	
	private final int minX;
	private final int maxX;
	private final int minY;
	private final int maxY;
	
	/**
	 * The associated game character.
	 */
	private final Player player;

	/**
	 * The destination the game character will move to.
	 */
	private Position destination;

	/**
	 * The task a player must execute upon reaching said destination.
	 */
	private final FinalizedMovementTask finalizedTask;
	
	/**
	 * Executes the action if distance is correct
	 */
	public void tick() {
		if(player == null)
			return;
		if(!player.isRegistered()) {
			player.setWalkToTask(null);
			return;
		}

		if(player.getWalkToTask() != null && player.getWalkToTask() == this) {
			if(player.isTeleporting() || player.getConstitution() <= 0 || destination == null) {
				player.setWalkToTask(null);
				return;
			}
			
			int pX = player.getPosition().getX();
			int pY = player.getPosition().getY();

			if (pX >= minX && pX <= maxX && pY >= minY && pY <= maxY) {

				finalizedTask.execute();
				player.setEntityInteraction(null);
				player.setWalkToTask(null);
			}
		}
	}
}
