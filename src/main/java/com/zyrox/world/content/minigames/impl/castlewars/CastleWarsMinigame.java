package com.zyrox.world.content.minigames.impl.castlewars;

import com.zyrox.model.GroundItem;
import com.zyrox.model.Item;
import com.zyrox.model.Position;
import com.zyrox.world.content.minigames.impl.castlewars.object.CastleWarsDoubleDoor;
import com.zyrox.world.content.minigames.impl.castlewars.object.CastleWarsSingleDoor;
import com.zyrox.world.content.minigames.impl.castlewars.object.catapult.CastleWarsCatapultManager;
import com.zyrox.world.content.minigames.impl.castlewars.object.rocks.CastleWarsCollapsingRockManager;
import com.zyrox.world.entity.impl.GroundItemManager;

/**
 * Represents a Castle Wars minigame
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 * 
 */
public class CastleWarsMinigame {

	/**
	 * Bucket of water
	 */
	public static final Item BUCKET_OF_WATER = new Item(1929);

	/**
	 * The zamorak bucket
	 */
	private static final GroundItem ZAMORAK_BUCKET = new GroundItem(BUCKET_OF_WATER, new Position(2369, 3124, 0), "",
			true, 1, true, 10);
	
	/**
	 * The saradomin bucket
	 */
	private static final GroundItem SARADOMIN_BUCKET = new GroundItem(BUCKET_OF_WATER, new Position(2430, 3083, 0), "",
			true, 1, true, 10);

	/**
	 * The rocks
	 */
	private boolean[] rocksCollapsed;

	/**
	 * Prepare the minigame
	 */
	public void prepare() {
		/*
		 * Set rocks
		 */
		rocksCollapsed = new boolean[4];
		/*
		 * Buckets
		 */
		GroundItemManager.add(ZAMORAK_BUCKET, false);
		GroundItemManager.add(SARADOMIN_BUCKET, false);
		/*
		 * Prepare catapult
		 */
		CastleWarsCatapultManager.prepare();
		/*
		 * Prepare doors
		 */
		CastleWarsSingleDoor.prepare();
		/*
		 * Prepare double door
		 */
		CastleWarsDoubleDoor.prepare();
		/*
		 * Prepare rocks
		 */
		CastleWarsCollapsingRockManager.prepare();
		/*
		 * Prepare saradomin team
		 */
		CastleWarsManager.saradomin.prepare();
		/*
		 * Prepare zamorak team
		 */
		CastleWarsManager.zamorak.prepare();
	}

	/**
	 * Gets the rocksCollapsed
	 *
	 * @return the rocksCollapsed
	 */
	public boolean[] getRocksCollapsed() {
		return rocksCollapsed;
	}
}