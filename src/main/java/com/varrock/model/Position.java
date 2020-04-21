package com.varrock.model;

import com.varrock.util.Misc;
import com.varrock.world.entity.impl.GameCharacter;

/**
 * Represents a single world tile position.
 * 
 * @author relex lawl
 */

public class Position {

	/**
	 * The Position constructor.
	 * @param x		The x-type coordinate of the position.
	 * @param y		The y-type coordinate of the position.
	 * @param z		The height of the position.
	 */
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * The Position constructor.
	 * @param x		The x-type coordinate of the position.
	 * @param y		The y-type coordinate of the position.
	 */
	public Position(int x, int y) {
		this(x, y, 0);
	}

	/**
	 * The x coordinate of the position.
	 */
	private int x;

	/**
	 * Gets the x coordinate of this position.
	 * @return	The associated x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x coordinate of this position.
	 * @return The Position instance.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * The y coordinate of the position.
	 */
	private int y;

	/**
	 * Gets the y coordinate of this position.
	 * @return	The associated y coordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y coordinate of this position.
	 * @return The Position instance.
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * The height level of the position.
	 */
	private int z;

	/**
	 * Gets the height level of this position.
	 * @return	The associated height level.
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Sets the height level of this position.
	 * @return The Position instance.
	 */
	public Position setZ(int z) {
		this.z = z;
		return this;
	}

	/**
	 * Sets the player's associated Position values.
	 * @param x	The new x coordinate.
	 * @param y	The new y coordinate.
	 * @param z The new height level.
	 */
	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setAs(Position other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}

	/**
	 * Gets the local x coordinate relative to a specific region.
	 * @param position	The region the coordinate will be relative to.
	 * @return 			The local x coordinate.
	 */
	public int getLocalX(Position position) {
		return x - 8 * position.getRegionX();
	}

	/**
	 * Gets the local y coordinate relative to a specific region.
	 * @param position 	The region the coordinate will be relative to.
	 * @return 			The local y coordinate.
	 */
	public int getLocalY(Position position) {
		return y - 8 * position.getRegionY();
	}

	/**
	 * Gets the local x coordinate relative to a specific region.
	 * @return 			The local x coordinate.
	 */
	public int getLocalX() {
		return x - 8 * getRegionX();
	}

	/**
	 * Gets the local y coordinate relative to a specific region.
	 * @return 			The local y coordinate.
	 */
	public int getLocalY() {
		return y - 8 * getRegionY();
	}

	/**
	 * Gets the region x coordinate.
	 * @return The region x coordinate.
	 */
	public int getRegionX() {
		return (x >> 3) - 6;
	}

	/**
	 * Gets the region y coordinate.
	 * @return The region y coordinate.
	 */
	public int getRegionY() {
		return (y >> 3) - 6;
	}

	/**
	 * Adds steps/coordinates to this position.
	 */
	public Position add(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}

	/**
	 * Adds steps/coordinates to this position.
	 */
	public Position add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Position transform(int diffX, int diffY, int diffZ) {
		return new Position(x + diffX, y + diffY, z + diffZ);
	}
	public Position transform(Position position) {
		return transform(position.getX(), position.getY(), position.getZ());
	}
	/**
	 * Checks if this location is within range of another.
	 * @param other The other location.
	 * @return <code>true</code> if the location is in range,
	 * <code>false</code> if not.
	 */
	public boolean isWithinDistance(Position other) {
		if(z != other.z)
			return false;
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
	}

	/**
	 * Checks if the position is within distance of another.
	 * @param other The other position.
	 * @param distance The distance.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isWithinDistance(Position other, int distance) {
		if(z != other.getZ())
			return false;
		int deltaX = Math.abs(x - other.x);
		int deltaY = Math.abs(y - other.y);
		if (getRegionID() == 9043) {
			return true;
		}
		return deltaX <= distance && deltaY <= distance;
	}
	
	public int getRegionID() {
		int regionX = getX() >> 3;
		int regionY = getY() >> 3;
		return ((regionX / 8) << 8) + (regionY / 8);
	}

	/**
	 * The region ID of the location you're in.
	 */

	public int getRegionId() {
		int regionX = x >> 6;
		int regionY = y >> 6;
		return regionX << 8 | regionY;
	}



	/**
	 * Checks if the attacker is in a certain distance from the target
	 *
	 * @param entity
	 * @param other
	 * @param req
	 * @return
	 */
	public static boolean isWithinDistance(GameCharacter entity, GameCharacter other, int req) {
		if (entity == null || other == null) {
			return false;
		}

		if (entity.getPosition().getZ() != other.getPosition().getZ()) {
			return false;
		}

		int x = entity.getPosition().getX();
		int y = entity.getPosition().getY();
		int x2 = other.getPosition().getX();
		int y2 = other.getPosition().getY();

		if (getManhattanDistance(x, y, x2, y2) <= req) {
			return true;
		}

		Position[] a = getBorder(entity.getPosition(), entity.getSize());
		Position[] b = getBorder(other.getPosition(), other.getSize());

		for (Position i : a) {
			for (Position k : b) {
				if (getManhattanDistance(i, k) <= req) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets Manhattan distance
	 *
	 * @param x
	 * @param y
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static int getManhattanDistance(int x, int y, int x2, int y2) {
		return Math.abs(x - x2) + Math.abs(y - y2);
	}

	/**
	 * Returns the manhattan distance between 2 positions.
	 *
	 * @param pos
	 * @param other
	 * @return
	 */
	public static int getManhattanDistance(Position pos, Position other) {
		return getManhattanDistance(pos.getX(), pos.getY(), other.getX(), other.getY());
	}

	public static Position[] getBorder(Position loc, int size) {
		return getBorder(loc.getX(), loc.getY(), size);
	}

	public static Position[] getBorder(int x, int y, int size) {
		if (size <= 1) {
			return new Position[]
					{new Position(x, y)};
		}

		Position[] border = new Position[4 * (size - 1)];
		int j = 0;

		border[0] = new Position(x, y);

		for (int i = 0; i < 4; i++) {
			for (int k = 0; k < (i < 3 ? size - 1 : size - 2); k++) {
				if (i == 0)
					x++;
				else if (i == 1)
					y++;
				else if (i == 2)
					x--;
				else if (i == 3) {
					y--;
				}
				border[(++j)] = new Position(x, y);
			}
		}
		return border;
	}


	/**
	 * Checks if this location is within interaction range of another.
	 * @param other The other location.
	 * @return <code>true</code> if the location is in range,
	 * <code>false</code> if not.
	 */
	public boolean isWithinInteractionDistance(Position other) {
		if(z != other.z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 2 && deltaX >= -3 && deltaY <= 2 && deltaY >= -3;
	}

	/**
	 * Gets the distance between this position and another position. Only X and
	 * Y are considered (i.e. 2 dimensions).
	 * @param other The other position.
	 * @return The distance.
	 */
	public int getDistance(Position other) {
		int deltaX = x - other.x;
		int deltaY = y - other.y;
		return (int) Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY));
	}

	/**
	 * Checks if {@code position} has the same values as this position.
	 * @param position	The position to check.
	 * @return			The values of {@code position} are the same as this position's.
	 */
	public boolean sameAs(Position position) {
		return position.x == x && position.y == y && position.z == z;
	}

	/**
	 * Checks if {@code position} has the same values as this position.
	 * @param position	The position to check.
	 * @return			The values of {@code position} are the same as this position's.
	 */
	public boolean sameAsMinusFloor(Position position) {
		return position.x == x && position.y == y;
	}

	public double distanceToPoint(int pointX, int pointY) {
		return Math.sqrt(Math.pow(x - pointX, 2)
				+ Math.pow(y - pointY, 2));
	}

	public Position copy() {
		return new Position(x, y, z);
	}

	@Override
	public String toString() {
		return "X="+x+" Y="+y+" Z="+z;
	}

	public String toStringFormated() {
		return "[ " + x + ", " + y + ", " + "]";
	}

	@Override
	public int hashCode() {
		return z << 30 | x << 15 | y;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Position)) {
			return false;
		}
		Position position = (Position) other;
		return position.x == x && position.y == y && position.z == z;
	}

    public boolean isViewableFrom(Position other) {
        if (this.getZ() != other.getZ())
            return false;
        Position p = Misc.delta(this, other);
        return p.x <= 14 && p.x >= -15 && p.y <= 14 && p.y >= -15;
    }

	/**
	 * Returns if the entity's block is within distance of the other entity's block.
	 *
	 * @param other
	 * @param size
	 * @param otherSize
	 * @return if is within distance
	 */
	public boolean isWithinDiagonalDistance(Position other, int size, int otherSize) {

		int e_offset_x = size - 1;
		int e_offset_y = size - 1;

		int o_offset_x = otherSize - 1;
		int o_offset_y = otherSize - 1;

		boolean inside_entity = (other.getX() <= x + e_offset_x && other.getX() >= (x)) && (other.getY() <= y + e_offset_y && other.getY() >= (y));

		boolean inside_other = (x <= other.getX() + o_offset_x && x >= (other.getX()) && (y <= other.getY() + o_offset_y && y >= (other.getY())));

		return inside_entity || inside_other;
	}

	/**
	 * Checks if this {@link Position} is within the boundary coordinates.
	 * @param minimumX	The boundary's minimum X-coordinate.
	 * @param maximumX	The boundary's maximum X-coordinate.
	 * @param minimumY	The boundary's minimum Y-coordinate.
	 * @param maximumY	The boundary's maximum Y-coordinate.
	 * @return			If {@code true} this {@link Position} is within the coordinates.
	 */
	public boolean isWithinBoundary(int minimumX, int maximumX, int minimumY, int maximumY) {
		return x >= minimumX && x <= maximumX && y >= minimumY && y <= maximumY;
	}

	public int distanceTo(int otherX, int otherY) {
		int minDistance = (int) Math.hypot(otherX - x, otherY - y);
		for (int x1 = x; x1 < x + 2 - 1; x1++) {
			for (int y1 = y; y1 < y + 2 - 1; y1++) {
				int distance = (int) Math.hypot(otherX - x1, otherY - y1);
				if (distance < minDistance) {
					minDistance = distance;
				}
			}
		}
		return minDistance;
	}

	/**
	 * Creates a new position from the given x, z, and y position.
	 *
	 * @param x the x offset of this translation.
	 * @param y the y offset of this translation.
	 * @param z the z offset of this translation.
	 * @return a new position object with the given translation.
	 */
	public Position translate(int x, int y, int z) {
		return new Position(this.x + x, this.y + y, this.z + z);
	}

	/**
	 * Creates a new position from the given x, z, and y position.
	 *
	 * @param x the x offset of this translation.
	 * @param y the y offset of this translation.
	 * @return a new position object with the given translation.
	 */
	public Position translate(int x, int y) {
		return new Position(this.x + x, this.y + y, this.z);
	}

	/**
	 * Creates a new position from the given z position.
	 *
	 * @param z the z of this translation.
	 * @return a new position object with the given translation.
	 */
	public Position translate(int z) {
		return new Position(this.x, this.y, z);
	}

}