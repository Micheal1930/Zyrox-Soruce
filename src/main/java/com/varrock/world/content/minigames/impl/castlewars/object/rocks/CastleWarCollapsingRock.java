package com.varrock.world.content.minigames.impl.castlewars.object.rocks;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import com.varrock.model.Boundary;
import com.varrock.model.Position;
import com.varrock.world.entity.impl.player.Player;

/**
 * The underground rocks
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 */
public enum CastleWarCollapsingRock {
	EAST(new Position(2409, 9503), new Boundary(new Position(2409, 9503), new Position(2410, 9504))),

	SOUTH(new Position(2401, 9494), new Boundary(new Position(2401, 9494), new Position(2402, 9495))),

	WEST(new Position(2391, 9501), new Boundary(new Position(2391, 9501), new Position(2392, 9502))),

	NORTH(new Position(2400, 9512), new Boundary(new Position(2400, 9512), new Position(2401, 9513))),

	;

	/**
	 * The rock position
	 */
	private Position position;

	/**
	 * The rock boundary
	 */
	private Boundary boundary;

	/**
	 * Represents an underground rock
	 * 
	 * @param position the position
	 * @param boundary the boundary
	 */
	private CastleWarCollapsingRock(Position position, Boundary boundary) {
		this.position = position;
		this.boundary = boundary;
	}

	/**
	 * The rocks
	 */
	public static final Set<CastleWarCollapsingRock> ROCKS = Collections
			.unmodifiableSet(EnumSet.allOf(CastleWarCollapsingRock.class));

	/**
	 * Gets the rock data by position
	 * 
	 * @param position the position
	 * @return the data
	 */
	public static Optional<CastleWarCollapsingRock> getRock(Position position) {
		return ROCKS.stream().filter(rock -> rock.getPosition().sameAs(position)).findFirst();
	}

	/**
	 * Gets the rock data by player
	 * 
	 * @param player the player
	 * @return the data
	 */
	public static Optional<CastleWarCollapsingRock> getWall(Player player) {
		return ROCKS.stream().filter(rock -> rock.getBoundary().inside(player, 3)).findFirst();
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
	 * Gets the boundary
	 *
	 * @return the boundary
	 */
	public Boundary getBoundary() {
		return boundary;
	}
}