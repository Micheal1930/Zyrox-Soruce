package com.zyrox.world.content.doors;

import com.zyrox.world.HashedPosition;
import com.zyrox.world.content.doors.DoorConstants.Direction;

/**
 *
 * @author Relex
 */
public final class DoorState {
	
	public DoorState(int id, Direction direction, HashedPosition position) {
		this.id = id;
		this.direction = direction;
		this.position = position;
	}
	
	private final int id;
	
	private final Direction direction;
	
	private final HashedPosition position;
	
	public int getId() {
		return id;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public HashedPosition getPosition() {
		return position;
	}
}