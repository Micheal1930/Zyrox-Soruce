package com.varrock.world.content.minigames.impl.castlewars.object.catapult;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import com.varrock.model.Position;

/**
 * Represents the Castle Wars catapult
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 * 
 */
public enum CastleWarsCatapult {

	ZAMORAK(4381, 4904, new Position(2384, 3117, 0), 3),

	SARADOMIN(4382, 4905, new Position(2413, 3088, 0), 0),

	;

	/**
	 * The id
	 */
	private int id;

	/**
	 * The fire id
	 */
	private int onFire;

	/**
	 * The position
	 */
	private Position position;
	
	/**
	 * The face
	 */
	private int face;

	/**
	 * Represents a catapult
	 * 
	 * @param id       the id
	 * @param onFire   the fire id
	 * @param position the position
	 */
	CastleWarsCatapult(int id, int onFire, Position position, int face) {
		this.setId(id);
		this.setOnFire(onFire);
		this.setPosition(position);
		this.face = face;
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
	 * Sets the id
	 *
	 * @param id the id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the onFire
	 *
	 * @return the onFire
	 */
	public int getOnFire() {
		return onFire;
	}

	/**
	 * Sets the onFire
	 *
	 * @param onFire the onFire
	 */
	public void setOnFire(int onFire) {
		this.onFire = onFire;
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
	 * Sets the position
	 *
	 * @param position the position
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
	
	/**
	 * Gets the face
	 *
	 * @return the face
	 */
	public int getFace() {
		return face;
	}

	/**
	 * The rocks
	 */
	public static final Set<CastleWarsCatapult> CATAPULT = Collections
			.unmodifiableSet(EnumSet.allOf(CastleWarsCatapult.class));

	/**
	 * Gets the catapult by id
	 * 
	 * @param id the id
	 * @return the data
	 */
	public static Optional<CastleWarsCatapult> getCatapult(int id) {
		return CATAPULT.stream().filter(c -> c.getId() == id).findFirst();
	}
	
	/**
	 * Gets the catapult by fire id
	 * 
	 * @param id the id
	 * @return the data
	 */
	public static Optional<CastleWarsCatapult> getOnFire(int id) {
		return CATAPULT.stream().filter(c -> c.getOnFire() == id).findFirst();
	}
	
	/**
	 * Gets the catapult by position
	 * 
	 * @param position the position
	 * @return the data
	 */
	public static Optional<CastleWarsCatapult> getCatapultByPosition(Position position) {
		return CATAPULT.stream().filter(c -> c.getPosition().sameAs(position)).findFirst();
	}
}
