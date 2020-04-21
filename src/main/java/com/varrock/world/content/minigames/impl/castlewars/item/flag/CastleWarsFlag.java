package com.varrock.world.content.minigames.impl.castlewars.item.flag;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import com.varrock.model.Position;

/**
 * Represents a Castle Wars Flag
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public enum CastleWarsFlag {

	ZAMORAK(4903, 4901, 4378, new Position(2370, 3130, 3), 4039),

	SARADOMIN(4902, 4900, 4377, new Position(2429, 3071, 3), 4037),

	;

	/**
	 * The captured object
	 */
	private int capture;

	/**
	 * The dropped object
	 */
	private int dropped;

	/**
	 * The stand object
	 */
	private int stand;

	/**
	 * The object flag position
	 */
	private Position position;

	/**
	 * The flag item id
	 */
	private int id;

	/**
	 * Represents a castle wars flag
	 * 
	 * @param capture  the capture
	 * @param dropped  the dropped
	 * @param stand    the stand
	 * @param position the position
	 * @param the      id the id
	 */
	CastleWarsFlag(int capture, int dropped, int stand, Position position, int id) {
		this.capture = capture;
		this.dropped = dropped;
		this.stand = stand;
		this.position = position;
		this.id = id;
	}

	/**
	 * Gets the capture
	 *
	 * @return the capture
	 */
	public int getCapture() {
		return capture;
	}

	/**
	 * Gets the dropped
	 *
	 * @return the dropped
	 */
	public int getDropped() {
		return dropped;
	}

	/**
	 * Gets the stand
	 *
	 * @return the stand
	 */
	public int getStand() {
		return stand;
	}

	/**
	 * Gets the position
	 *
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the id
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * The rocks
	 */
	public static final Set<CastleWarsFlag> FLAGS = Collections.unmodifiableSet(EnumSet.allOf(CastleWarsFlag.class));

	/**
	 * Gets the flag capture
	 * 
	 * @return the data
	 */
	public static Optional<CastleWarsFlag> getCapture(int object) {
		return FLAGS.stream().filter(flag -> flag.getCapture() == object).findFirst();
	}

	/**
	 * Gets the flag dropped
	 * 
	 * @return the data
	 */
	public static Optional<CastleWarsFlag> getDropped(int object) {
		return FLAGS.stream().filter(flag -> flag.getDropped() == object).findFirst();
	}

	/**
	 * Get for flag score
	 * 
	 * @return the data
	 */
	public static Optional<CastleWarsFlag> getForScore(int object) {
		return FLAGS.stream().filter(flag -> (flag.getDropped() == object || flag.getCapture() == object)).findFirst();
	}

	/**
	 * Gets the flag by id
	 * 
	 * @return the data
	 */
	public static Optional<CastleWarsFlag> getForId(int id) {
		return FLAGS.stream().filter(flag -> flag.getId() == id).findFirst();
	}
}
