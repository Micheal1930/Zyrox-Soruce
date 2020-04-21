package com.varrock.world.content.minigames.impl.castlewars.object;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Flag;
import com.varrock.model.Position;
import com.varrock.world.entity.impl.player.Player;

/**
 * Handles Castle Wars stepping stones
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class CastleWarsSteppingStone {

	/**
	 * The animation
	 */
	private static final int STEPPING_STONE_ANIMATION = 769;

	/**
	 * The stepping stone
	 */
	public static final int STEPPING_STONE = 4411;

	/**
	 * Handles stepping stones
	 * 
	 * @param player the player
	 * @param id     the object id
	 * @param x      the object x
	 * @param y      the object y
	 * @return stepping stones
	 */
	public static void handleSteppingStone(Player player, int id, int x, int y) {
		int obX = x;
		int obY = y;
		int myX = player.getPosition().getX();
		int myY = player.getPosition().getY();
		int toX = 0;
		int toY = 0;
		if (obX == 2378 && obY == 3084) {
			if (myX == 2378 && myY == 3085)
				toY = -1;
			if (myX == 2378 && myY == 3083)
				toY = 1;
		} else if (obX == 2378 && obY == 3085) {
			if (myX == 2378 && myY == 3084)
				toY = 1;
			if (myX == 2377 && myY == 3085)
				toX = 1;
		} else if (obX == 2377 && obY == 3085) {
			if (myX == 2378 && myY == 3085)
				toX = -1;
			if (myX == 2377 && myY == 3086)
				toY = -1;
		} else if (obX == 2377 && obY == 3086) {
			if (myX == 2377 && myY == 3085)
				toY = 1;
			if (myX == 2377 && myY == 3087)
				toY = -1;
		} else if (obX == 2377 && obY == 3087) {
			if (myX == 2377 && myY == 3086)
				toY = 1;
			if (myX == 2377 && myY == 3088)
				toY = -1;
		} else if (obX == 2377 && obY == 3088) {
			if (myX == 2377 && myY == 3087)
				toY = 1;
			if (myX == 2377 && myY == 3089)
				toY = -1;
		} else if (obX == 2419 && obY == 3125) {
			if (myX == 2418 && myY == 3125)
				toX = 1;
			if (myX == 2419 && myY == 3124)
				toY = 1;
		} else if (obX == 2419 && obY == 3124) {
			if (myX == 2419 && myY == 3125)
				toY = -1;
			if (myX == 2419 && myY == 3123)
				toY = 1;
		} else if (obX == 2419 && obY == 3123) {
			if (myX == 2419 && myY == 3124)
				toY = -1;
			if (myX == 2420 && myY == 3123)
				toX = -1;
		} else if (obX == 2420 && obY == 3123) {
			if (myX == 2420 && myY == 3122)
				toY = 1;
			if (myX == 2419 && myY == 3123)
				toX = 1;
		} else if (obX == 2418 && obY == 3125) {
			if (myX == 2418 && myY == 3126) {
				toY = -1;
			}
			if (myX == 2419 && myY == 3125) {
				toX = -1;
			}
		}
		/*
		 * Unreachable
		 */
		if ((toX < -1 && toX > 1) || (toY < -1 && toY > 1) || (toX == 0 && toY == 0)) {
			player.getPacketSender().sendMessage("You can't reach that.");
			return;
		} /*
			 * Player moving
			 */
		move(player, myX + toX, myY + toY, toX, toY);
	}

	/**
	 * Moving across stepping stone
	 * 
	 * @param player  the player
	 * @param x       the x
	 * @param y       the y
	 * @param xOffset the x offset
	 * @param yOffset the y offset
	 */
	private static void move(Player player, int x, int y, int xOffset, int yOffset) {
		/*
		 * Already in action
		 */
		if (player.getSkillAnimation() > 0) {
			return;
		}
		/*
		 * Set the animation
		 */
		player.setSkillAnimation(STEPPING_STONE_ANIMATION);
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		player.getPacketSender().sendMessage("You jump across the stepping stone..");
		player.getMovementQueue().walkStep(xOffset, yOffset);
		TaskManager.submit(new Task(1, player, false) {

			@Override
			public void execute() {
				player.moveTo(new Position(x, y));
				stop();
			}

			@Override
			public void stop() {
				setEventRunning(false);
				player.setSkillAnimation(-1);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			}
		});
	}
}
